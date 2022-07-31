@file:OptIn(ExperimentalAnimationApi::class)

package com.example.project_am_manager.presentation.ui.compose.utils

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry

fun enterTransition(
    animationSpec: Int,
    page1: String,
    direction1: AnimatedContentScope.SlideDirection,
    page2: String,
    direction2: AnimatedContentScope.SlideDirection,
    _this: AnimatedContentScope<NavBackStackEntry>
): EnterTransition? {
    return with(_this){
        when (_this.initialState.destination.route) {
            page1 ->
                slideIntoContainer(
                    direction1,
                    animationSpec = tween(animationSpec)
                )
            page2 ->
                slideIntoContainer(
                    direction2,
                    animationSpec = tween(animationSpec)
                )
            else -> null
        }
    }
}
fun exitTransition(
    animationSpec: Int,
    page1: String,
    direction1: AnimatedContentScope.SlideDirection,
    page2: String,
    direction2: AnimatedContentScope.SlideDirection,
    _this: AnimatedContentScope<NavBackStackEntry>
): ExitTransition? {
    return with(_this){
        when (_this.initialState.destination.route) {
            page1 ->
                slideOutOfContainer(
                    direction1,
                    animationSpec = tween(animationSpec)
                )
            page2 ->
                slideOutOfContainer(
                    direction2,
                    animationSpec = tween(animationSpec)
                )
            else -> null
        }
    }
}