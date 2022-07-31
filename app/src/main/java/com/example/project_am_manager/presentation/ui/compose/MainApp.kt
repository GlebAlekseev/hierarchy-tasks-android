@file:OptIn(ExperimentalAnimationApi::class, ExperimentalAnimationApi::class,
    ExperimentalAnimationApi::class
)

package com.example.project_am_manager.presentation.ui.compose

import androidx.compose.animation.*
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Right
import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.project_am_manager.presentation.ui.compose.components.floatingactionbutton.MainExtendedFloatingActionButton
import com.example.project_am_manager.presentation.ui.compose.components.modalbottomsheet.ModalBottomSheetChoose
import com.example.project_am_manager.presentation.ui.compose.components.topbar.HierarchyTopBar
import com.example.project_am_manager.presentation.ui.compose.components.topbar.HistoryTopBar
import com.example.project_am_manager.presentation.ui.compose.components.topbar.HomeTopBar
import com.example.project_am_manager.presentation.ui.compose.screens.main.HierarchyScreen
import com.example.project_am_manager.presentation.ui.compose.screens.main.HistoryScreen
import com.example.project_am_manager.presentation.ui.compose.screens.main.HomeScreen
import com.example.project_am_manager.presentation.ui.compose.struct.NavigationItem
import com.example.project_am_manager.presentation.ui.compose.utils.transition
import com.example.project_am_manager.presentation.viewmodel.MainViewModel
import com.example.project_am_manager.ui.theme.MainTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import routing.MainRouter
import routing.Screen

@Composable
fun MainApp(viewModel: MainViewModel) {
    MainTheme {
        AppContent(viewModel)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppContent(viewModel: MainViewModel) {
    viewModel.setAnimateNavController(rememberAnimatedNavController())
    viewModel.setPagerState(rememberPagerState())

    Crossfade(targetState = MainRouter.currentScreen) { screenState: MutableState<Screen> ->
        viewModel.setScreenStateMain(screenState.value)
        Scaffold(
            bottomBar = { GetBottomNavigationComponent(viewModel) },
            content = { NavigateContainer(viewModel) },
            floatingActionButton = { MainExtendedFloatingActionButton(viewModel) }
        )
    }
    ModalBottomSheetChoose(viewModel)
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigateContainer(viewModel: MainViewModel) {
    val animateNavController by viewModel.animateNavController.collectAsState()
    AnimatedNavHost(animateNavController!!, startDestination = "Home") {
        composable(
            "Home",
            enterTransition = {
                transition(300, "Hierarchy", Right, "History", Right, this)
            },
            exitTransition = {
                transition(300, "Hierarchy", Left, "History", Left, this)
            }
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
                transition(300, "Home", Left, "History", Right, this)
            },
            exitTransition = {
                transition(300, "Home", Right, "History", Left, this)
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
                transition(300, "Home", Left, "Hierarchy", Left, this)
            },
            exitTransition = {
                transition(300, "Home", Right, "Hierarchy", Right, this)
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


@Composable
fun GetBottomNavigationComponent(
    viewModel: MainViewModel
) {
    val animateNavController by viewModel.animateNavController.collectAsState()
    val selectedItem by viewModel.selectedItem.collectAsState()
    BottomNavigation {
        NavigationItem.items.forEach {
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
                                        Color(135, 138, 245),
                                        Offset(-size.width, -size.height * 0.4f / 2),
                                        Size(size.width * 3f, size.height * 1.4f),
                                        cornerRadius = CornerRadius(50f, 50f),
                                        alpha = 0.1f
                                    )

                                }
                            }
                    )
                }
            )
        }
    }
}