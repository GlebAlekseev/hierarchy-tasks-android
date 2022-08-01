package com.example.project_am_manager.presentation.ui.compose.content

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
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
                when (initialState.destination.route) {
                    Hierarchy ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    History ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Hierarchy ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    History ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
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
                when (initialState.destination.route) {
                    Home ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    History ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Home ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    History ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
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
                when (initialState.destination.route) {
                    Home ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    Hierarchy ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Home ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    Hierarchy ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
        ) {
            viewModel.setScreenState(MainScreen.History)
            Column {
                MainHistoryScreen(viewModel)
            }
        }
    }
}
