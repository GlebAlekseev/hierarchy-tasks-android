package com.example.project_am_manager.presentation.ui.compose.content

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.example.project_am_manager.presentation.ui.compose.screen.main.MainHierarchyScreen
import com.example.project_am_manager.presentation.ui.compose.screen.main.MainHistoryScreen
import com.example.project_am_manager.presentation.ui.compose.screen.main.MainHomeScreen
import com.example.project_am_manager.presentation.ui.compose.topbar.MainHierarchyTopBar
import com.example.project_am_manager.presentation.ui.compose.topbar.MainHomeTopBar
import com.example.project_am_manager.presentation.ui.compose.utils.enterTransition
import com.example.project_am_manager.presentation.ui.compose.utils.exitTransition
import com.example.project_am_manager.presentation.viewmodel.MainViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import routing.MainScreen


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainContent(viewModel: MainViewModel) {
    viewModel.setAnimateNavController(rememberAnimatedNavController())
    val animateNavController by viewModel.animateNavController.collectAsState()
    val Home = stringResource(id = MainScreen.Home.titleResId)
    val Hierarchy = stringResource(id = MainScreen.Hierarchy.titleResId)
    val History = stringResource(id = MainScreen.History.titleResId)

    AnimatedNavHost(animateNavController!!, startDestination = Home) {
        composable(
            Home,
            enterTransition = {
                enterTransition(
                    300,
                    Hierarchy,
                    AnimatedContentScope.SlideDirection.Right,
                    History,
                    AnimatedContentScope.SlideDirection.Right,
                    this
                )
            },
            exitTransition = {
                exitTransition(
                    300,
                    Hierarchy,
                    AnimatedContentScope.SlideDirection.Left,
                    History,
                    AnimatedContentScope.SlideDirection.Left,
                    this
                )
            }
        ) {
            viewModel.setScreenState(MainScreen.Home)
            Column() {
                MainHomeTopBar(viewModel)
                MainHomeScreen(viewModel)
            }
        }
        composable(
            Hierarchy,
            enterTransition = {
                enterTransition(
                    300,
                    Home,
                    AnimatedContentScope.SlideDirection.Left,
                    History,
                    AnimatedContentScope.SlideDirection.Right,
                    this
                )
            },
            exitTransition = {
                exitTransition(
                    300,
                    Home,
                    AnimatedContentScope.SlideDirection.Right,
                    History,
                    AnimatedContentScope.SlideDirection.Left,
                    this
                )
            },
        ) {
            viewModel.setScreenState(MainScreen.Hierarchy)
            Column() {
                MainHierarchyTopBar(viewModel)
                MainHierarchyScreen(viewModel)
            }
        }
        composable(
            History,
            enterTransition = {
                enterTransition(
                    300,
                    Home,
                    AnimatedContentScope.SlideDirection.Left,
                    Hierarchy,
                    AnimatedContentScope.SlideDirection.Left,
                    this
                )
            },
            exitTransition = {
                exitTransition(
                    300,
                    Home,
                    AnimatedContentScope.SlideDirection.Right,
                    Hierarchy,
                    AnimatedContentScope.SlideDirection.Right,
                    this
                )
            },
        ) {
            viewModel.setScreenState(MainScreen.History)
            Column {
                MainHistoryScreen(viewModel)
            }
        }
    }
}
