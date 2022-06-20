package com.example.project_am_manager

import android.content.Intent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.RequestDisallowInterceptTouchEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.project_am_manager.ui.theme.Project_AM_ManagerTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.skyyo.expandablelist.cards.BoardsScreen
import components.floatingactionbutton.MainExtendedFloatingActionButton
import components.modalbottomsheet.ModalBottomSheetChoose
import components.topbar.HierarchyTopBar
import components.topbar.HistoryTopBar
import components.topbar.HomeTopBar
import domain.model.BoardModel
import domain.model.TaskModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import routing.Screen
import routing.TManagerRouter
import screens.HierarchyScreen
import screens.HistoryScreen
import screens.HomeScreen
import viewmodel.MainViewModel

@Composable
fun TManagerApp(viewModel: MainViewModel) {
    Project_AM_ManagerTheme{
        AppContent(viewModel)
    }
}

private data class NavigationItem(
    val index: Int,
    val vectorResourceId: Int,
    val contentDescriptionResourceId: Int,
    val screen: String
)

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun AppContent(viewModel: MainViewModel) {
    val currentBoardId by viewModel.currentBoardId.collectAsState()
    val parentBoardId by viewModel.parentBoardId.collectAsState()

//    val data = DataMain(
//        scaffoldState = rememberScaffoldState(),
//        state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
//        scope = rememberCoroutineScope(),
//        currentBoard = remember { mutableStateOf(1L) },
//        boardParentId = remember { mutableStateOf(1L) },
//        viewModel = viewModel,
//        pagerState = rememberPagerState(),
//        index_toAnimateGo = remember {mutableStateOf(1)},
//        stateModal = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
//        openDialogEditing = remember { mutableStateOf(false)  },
//        scale_content = remember { mutableStateOf(0.4f) },
//        offset_content = remember { mutableStateOf(Offset(0f,0f)) },
//        isSelected = remember { mutableStateOf(false)},
//        screenState = TManagerRouter.currentScreen,
//        navController = rememberAnimatedNavController(),
//        selectedItem = remember { mutableStateOf(0) }
//    )

    viewModel.setAnimateNavController(rememberAnimatedNavController())
    viewModel.setPagerState(rememberPagerState())



    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
    val allTasks: List<TaskModel> by viewModel.allTasks.observeAsState(emptyList())

//    viewModel.setParentBoardId(allBoards.filter { it.id == currentBoardId }.lastOrNull()
//        .let { if (it != null) it.parent_id else 1 })

//    viewModel.setIndexAnimateTarget(allBoards.filter { a ->
//        (a.parent_id == parentBoardId && !allTasks.filter { it.parent_id == a.id }
//            .isNullOrEmpty())
//    }
//        .indexOf(if (allBoards.filter { it.id == currentBoardId }
//                .lastOrNull() != null) allBoards.filter { it.id == currentBoardId }
//            .lastOrNull() else BoardModel(0, "", "", 0))
//        .let { if (it == -1) 0 else it })

    Crossfade(targetState = TManagerRouter.currentScreen) { screenState: MutableState<Screen> ->
        viewModel.setScreenStateMain(screenState.value)
        Scaffold(
            bottomBar = { GetBottomNavigationComponent(viewModel) },
            content = { NavigateContainer(viewModel) },
            floatingActionButton = { MainExtendedFloatingActionButton(viewModel) }
        )
    }
    ModalBottomSheetChoose(viewModel)
}

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun NavigateContainer(viewModel: MainViewModel) {
    val animateNavController by viewModel.animateNavController.collectAsState()

    AnimatedNavHost(animateNavController!!, startDestination = "Home") {
        composable(
            "Home",
            enterTransition = {
                when (initialState.destination.route) {
                    "Hierarchy" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    "History" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "Hierarchy" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    "History" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
        ) {
            viewModel.setScreenStateMain(Screen.Home)
            Column() {
                HomeTopBar(viewModel)
                HomeScreen(viewModel)
            }
        }
        composable(
            "Hierarchy",
            enterTransition = {
                when (initialState.destination.route) {
                    "Home" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    "History" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "Home" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    "History" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
        ) {
            viewModel.setScreenStateMain(Screen.Hierarchy)
            Column() {
                HierarchyTopBar(viewModel)
                HierarchyScreen(viewModel)
            }

        }
        composable(
            "History",
            enterTransition = {
                when (initialState.destination.route) {
                    "Home" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    "Hierarchy" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "Home" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    "Hierarchy" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
        ) {
            viewModel.setScreenStateMain(Screen.History)
            Column {
                HistoryTopBar()
                HistoryScreen(viewModel)
            }

        }


    }

}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun GetBottomNavigationComponent(
  viewModel: MainViewModel
) {
    val animateNavController by viewModel.animateNavController.collectAsState()
    val selectedItem by viewModel.selectedItem.collectAsState()

    val items = listOf(
        NavigationItem(0, R.drawable.ic_baseline_home_24, R.string.home, "Home"),
        NavigationItem(1, R.drawable.ic_baseline_hierarchy_24, R.string.hierarchy, "Hierarchy"),
        NavigationItem(2, R.drawable.ic_baseline_history_24, R.string.history, "History"),
    )
    BottomNavigation {
        items.forEach {
            BottomNavigationItem(
                selected = selectedItem == it.index,
                onClick = {
                    viewModel.setSelectedItem(it.index)
                    animateNavController!!.navigate(it.screen)
                },
                icon = {
                    Icon(imageVector = ImageVector.vectorResource(
                        id = it.vectorResourceId
                    ),
                        contentDescription = stringResource(id = it.contentDescriptionResourceId),
                        tint = Color.Black,
                        modifier = Modifier
                            .drawBehind {
                                if (selectedItem == it.index) {
                                    drawRoundRect(
                                        Color(android.graphics.Color.parseColor("#F0F0F0")),
                                        Offset(-size.width, -size.height * 0.4f / 2),
                                        Size(size.width * 3f, size.height * 1.4f),
                                        cornerRadius = CornerRadius(50f, 50f)
                                    )
                                }
                            }
                    )
                }
            )
        }
    }
}