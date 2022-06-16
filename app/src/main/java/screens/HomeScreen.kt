package screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_am_manager.MainActivity
import com.example.project_am_manager.ModalBottomSheetChoose
import com.example.project_am_manager.R
import com.google.accompanist.pager.*
import data.database.dbmapper.DbMapperBoard
import data.database.dbmapper.DbMapperBoardImpl
import data.database.model.BoardDbModel
import domain.model.BoardModel
import domain.model.TaskModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import routing.BackButtonAction
import routing.TManagerRouter
import screens.task.TaskActivity
import viewmodel.MainViewModel

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    boardParentId: MutableState<Long>,
    currentBoard: MutableState<Long>,
    pagerState: PagerState,
    isSelected: MutableState<Boolean>
) {

    // Выбрать всех у кого parent_id == current
    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
    val allTasks: List<TaskModel> by viewModel.allTasks.observeAsState(emptyList())
//    allBoards.filter { it.id==currentBoard }.firstOrNull().let { if (it != null) it.name else "" }



    BackButtonAction {
        TManagerRouter.goBack()
    }
//    val tabs = listOf(
//        TabItem.Home,
//        TabItem.History,
//        TabItem.Hierarchy
//    )


    val scope = rememberCoroutineScope()
//    var currentSubBoard:Long = allBoards.filter { it.parent_id == currentBoard }.firstOrNull().let { if (it != null) it.id else 1}
//



//    val pagerState = rememberPagerState(1)

    Column(modifier = Modifier.fillMaxSize()) {

        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            },

            ) {
                allBoards.filter { a ->
                    (a.parent_id == boardParentId.value && !allTasks.filter { it.parent_id == a.id }
                        .isNullOrEmpty()) && a.id != boardParentId.value
                }.forEachIndexed { index, board ->
                    Log.d("TTT", board.name)
                    Tab(
                        text = { Text(text = board.name) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
//                            currentSubBoard = board.id
//                            Log.d("TFT", currentSubBoard.toString())
                            }
                        })


                }
        }
        HorizontalPager(count = allBoards.filter {a->  (a.parent_id == boardParentId.value && !allTasks.filter{ it.parent_id == a.id } .isNullOrEmpty()) && a.id != boardParentId.value }.size, state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            LazyColumn(modifier = Modifier.fillMaxHeight()){
                items(items=allTasks.filter {
                    it.parent_id == allBoards.filter {a->  (a.parent_id == boardParentId.value && !allTasks.filter{ it.parent_id == a.id } .isNullOrEmpty()) && a.id != boardParentId.value }.get(page).id },
                    itemContent = { item->
                    ToastContent(viewModel,item,isSelected)
                })
            }
        }
    }




}

//
//typealias ComposableFun = @Composable () -> Unit
//sealed class TabItem(var icon: Int, var title: String, var screen: ComposableFun) {
//    object History : TabItem(R.drawable.ic_baseline_history_24, "History", { HistoryTab() })
//    object Hierarchy : TabItem(R.drawable.ic_baseline_hierarchy_24, "Hierarchy", { HierarchyTab() })
//    object Home : TabItem(R.drawable.ic_baseline_home_24, "Home", { HomeTab() })
//}
//





@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToastContent(viewModel: MainViewModel,item: TaskModel,isSelected: MutableState<Boolean>) {
    val shape = RoundedCornerShape(4.dp)
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(vertical = 20.dp)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, Color.Black, shape)
            .height(40.dp)
            .padding(horizontal = 8.dp)
            .combinedClickable(

                onClick = {

                    // Если SELECT закрыт
                    val intent = Intent(context, TaskActivity::class.java)
                    intent.putExtra(MainActivity.TRANSMITTED_ID, item.id)

//                Log.d("CHECKK", intent.getLongExtra(MainActivity.TRANSMITTED_ID).toString())
                    context.startActivity(intent)

                    // Если SELECT открыт
                    // выделить элемент
                },
                onLongClick = {
                    // Замена табов на действия над таском
                    // Состояние выбора для нескольких.
                    // Если SELECT открыт, тогда ничего, иначе открыть SELECT

                    // Удалить контекст
                    if (expanded){
                        expanded = false
                    }else {
                        expanded = true
                    }

                }

            ),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_baseline_plus_24
                ),
                contentDescription = item.name
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = item.description)
        }
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false  },modifier = Modifier
    ) {

        DropdownMenuItem(onClick = {

            viewModel.deleteTask(item)
            expanded = false
        }){
            Text("Удалить")
        }

    }
}

@Composable
fun HierarchyTab(){
    Text(text = "HierarchyTab")
}

@Composable
fun HomeTab(){
    Text(text = "HomeTab")
}