package com.example.project_am_manager

import android.app.Activity
import android.content.Intent
import android.icu.number.Scale
import android.util.Log
import android.view.MotionEvent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.RequestDisallowInterceptTouchEvent
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.ui.theme.Project_AM_ManagerTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.skyyo.expandablelist.cards.BoardsScreen
import com.skyyo.expandablelist.cards.CardsViewModel
import domain.model.BoardModel
import domain.model.TaskModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import routing.BackButtonAction
import routing.BackButtonHandler
import routing.Screen
import routing.TManagerRouter
import screens.HierarchyScreen
import screens.HistoryScreen
import screens.HomeScreen
import screens.ModalBottomSheetChooseHierarchy
import screens.task.TaskActivity
import viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TManagerApp(viewModel: MainViewModel,cardsViewModel: CardsViewModel) {

    Project_AM_ManagerTheme{
        AppContent(viewModel,cardsViewModel)
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun AppContent(viewModel: MainViewModel,cardsViewModel: CardsViewModel){

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var currentBoard = remember { mutableStateOf(1L) }
    val boardParentId =  remember { mutableStateOf(1L) }
    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
    val allTasks: List<TaskModel> by viewModel.allTasks.observeAsState(emptyList())
    boardParentId.value = allBoards.filter { it.id == currentBoard.value}.lastOrNull().let { if (it != null) it.parent_id else 1 }


    val pagerState = rememberPagerState()
    val index_toAnimateGo = remember {
        mutableStateOf(1)
    }
    index_toAnimateGo.value =allBoards.filter {a->  (a.parent_id == boardParentId.value && !allTasks.filter{ it.parent_id == a.id } .isNullOrEmpty()) && a.id != boardParentId.value }
        .indexOf(if (allBoards.filter { it.id == currentBoard.value }.lastOrNull()!=null) allBoards.filter { it.id == currentBoard.value }.lastOrNull() else BoardModel(0,"","",0) )
        .let { if (it == -1) 0 else it }


//    val SHD = LocalConfiguration.current.screenHeightDp-165f
//    var offset  =  remember { mutableStateOf(Offset(0f,SHD)) }


//    viewModel.insertBoard(BoardModel(0,"under root 1",    SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(
//        Date()
//    ),1))
//    viewModel.insertBoard(BoardModel(0,"under root 2",    SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(
//        Date()
//    ),1))
//    viewModel.insertBoard(BoardModel(0,"under root 3",    SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(
//        Date()
//    ),1))

//        viewModel.insertTask(
//            TaskModel(0,"sub id=2 task 1","description sub 2",SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(
//                Date()), Color.Magenta,2     )
//        )
//    viewModel.insertTask(
//        TaskModel(0,"sub id=2 task 2","description sub 2",SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(
//            Date()),Color.Magenta,2     )
//    )
//    viewModel.insertTask(
//        TaskModel(0,"sub id=2 task 3","description sub 2",SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(
//            Date()), Color.Magenta,4     )
//    )
    // Анимация?!
    val stateModal = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val openDialogEditing = remember { mutableStateOf(false)  }
    var scale_content = remember { mutableStateOf(0.6f) }
    var offset_content = remember { mutableStateOf(Offset.Zero) }






    val isSelected = remember {
        mutableStateOf(false)
    }



    Crossfade(targetState = TManagerRouter.currentScreen) { screenState: MutableState<Screen> ->

        Scaffold(
            topBar = {TopAppBar(screenState,state,viewModel,boardParentId,currentBoard, scale_content,offset_content)},
            bottomBar = { BottomNavigationComponent(screenState) },
            content = { MainScreenContainer(screenState,viewModel ,boardParentId, currentBoard,pagerState,scale_content,offset_content,stateModal,openDialogEditing,isSelected) },
            floatingActionButton = {ExtendedFloatingActionButton(screenState)}

        )

    }
    ModalBottomSheetChoose(state,scope,viewModel,currentBoard,pagerState,index_toAnimateGo)

}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExtendedFloatingActionButton(screenState: MutableState<Screen>){
    val context = LocalContext.current
    if (screenState.value == Screen.Home){
        FloatingActionButton(
            content = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_plus_24), contentDescription = "")},
            onClick = {
                      context.startActivity(Intent(context,TaskActivity::class.java))
            },
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        )

    }


}




@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MainScreenContainer(
    screenState: MutableState<Screen>,
    viewModel: MainViewModel,
    boardParentId: MutableState<Long>,
    currentBoard: MutableState<Long>,
    pagerState: PagerState,
    scale_content: MutableState<Float>,
    offset_content: MutableState<Offset>,
    stateModal: ModalBottomSheetState,
    openDialogEditing: MutableState<Boolean>,
    isSelected: MutableState<Boolean>
) {
    val requestDisallowInterceptTouchEvent = RequestDisallowInterceptTouchEvent()
    requestDisallowInterceptTouchEvent.invoke(true)
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier,


    ) {
        when (screenState.value) {
            Screen.Home -> HomeScreen(viewModel,boardParentId,currentBoard,pagerState,isSelected)
            Screen.Hierarchy -> HierarchyScreen(screenState,viewModel,currentBoard,scale_content,offset_content, stateModal = stateModal,openDialogEditing)
            Screen.History -> HistoryScreen(viewModel)

        }
    }

}

@Composable
fun BottomNavigationComponent( screenState: MutableState<Screen>) {
    var selectedItem by remember { mutableStateOf(0) }

    val colors = MaterialTheme.colors

    val items = listOf(
        NavigationItem(0,R.drawable.ic_baseline_home_24,R.string.home, Screen.Home),
        NavigationItem(1,R.drawable.ic_baseline_hierarchy_24,R.string.hierarchy, Screen.Hierarchy),
        NavigationItem(2,R.drawable.ic_baseline_history_24,R.string.history, Screen.History),
    )
    BottomNavigation{
        items.forEach {
            BottomNavigationItem(
                selected = selectedItem == it.index,
                onClick = {
                    selectedItem = it.index
                    screenState.value = it.screen
                },
                icon = {Icon(imageVector = ImageVector.vectorResource(
                    id = it.vectorResourceId),
                    contentDescription = stringResource(id = it.contentDescriptionResourceId)

            )}
            )

        }

    }
}

private data class NavigationItem(
    val index: Int,
    val vectorResourceId: Int,
    val contentDescriptionResourceId: Int,
    val screen: Screen
)


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopAppBar(
    screenState: MutableState<Screen>,
    state: ModalBottomSheetState,
    viewModel: MainViewModel,
    boardParentId: MutableState<Long>,
    currentBoard: MutableState<Long>,
    scale_content: MutableState<Float>,
    offset_content: MutableState<Offset>,
){
    when (screenState.value) {

        Screen.Home ->   HomeTopBar(state,viewModel,boardParentId)
        Screen.Hierarchy -> HierarchyTopBar(viewModel,currentBoard,scale_content,offset_content)
        Screen.History -> HistoryTopBar()

    }
}


@Composable
fun HistoryTopBar() {

}

@Composable
fun HierarchyTopBar(
    viewModel: MainViewModel,
    currentBoard: MutableState<Long>,
    scale_content: MutableState<Float>,
    offset_content: MutableState<Offset>
) {
    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
    val SHD = LocalConfiguration.current.screenHeightDp-165f
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = allBoards.filter { it.id ==  currentBoard.value}.lastOrNull()?.name.orEmpty())
        IconButton(onClick = {
                scale_content.value = 0.6f
                offset_content.value = Offset(0f,0f)


        }) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_hierarchy_24), contentDescription ="" )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeTopBar(state: ModalBottomSheetState,viewModel: MainViewModel,currentBoard: MutableState<Long>) {
    val scope = rememberCoroutineScope()
    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
    // Текущая выбранная доска
    // Вывести все доски, где тасок больше 0

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(modifier = Modifier.clickable {
            if (state.isVisible){
                scope.launch { state.hide() }
            }else{
                scope.launch { state.show() }
            }
        }){
            Text(text = allBoards.filter { it.id==currentBoard.value }.firstOrNull().let { if (it != null) it.name else "" })
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_down_24), contentDescription = "", modifier = Modifier.padding(
                PaddingValues(start = 10.dp)))
        }
        Row() {
            var expanded by remember { mutableStateOf(false) }
            val items = listOf("A", "B", "C", "D", "E", "F")
            var selectedIndex by remember { mutableStateOf(0) }

            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_search_24), contentDescription = "",
                modifier = Modifier.padding(PaddingValues(end = 20.dp)))
//            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_more_24), contentDescription = "",
//                modifier=Modifier.clickable(onClick = { expanded = true }))
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false  },modifier = Modifier
                ) {
                        items.forEachIndexed { index, s ->
                            DropdownMenuItem(onClick = {
                                selectedIndex = index
                                expanded = false
                            }){
                                Text(text = s + " =sSs")
                            }
                        }
            }
        }


    }

}

@Composable
fun DropdownDemo() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("A", "B", "C", "D", "E", "F")
    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) }
    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.TopStart)) {
        Text(items[selectedIndex],modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { expanded = true })
            .background(
                Color.Gray
            ))
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.Red
                )
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                }) {
                    val disabledText = if (s == disabledValue) {
                        " (Disabled)"
                    } else {
                        ""
                    }
                    Text(text = s + disabledText)
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun ModalBottomSheetChoose(
    state: ModalBottomSheetState,
    scope: CoroutineScope,
    viewModel: MainViewModel,
    currentBoard: MutableState<Long>,
    pagerState: PagerState,
    index_toAnimateGo:MutableState<Int>
) {

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {

            BoardsScreen(index_toAnimateGo,pagerState,state = state,viewModel = viewModel, isAll = false,currentBoard=currentBoard)
        }
    ) {

        
    }


}


//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun ModalBottomSheetChooseAllBoards(state: ModalBottomSheetState,scope: CoroutineScope,viewModel: MainViewModel) {
//
//    ModalBottomSheetLayout(
//        sheetState = state,
//        sheetContent = {
//
//            BoardsScreen(viewModel,true)
//        }
//    ) {
//
//
//    }
//
//
//}










