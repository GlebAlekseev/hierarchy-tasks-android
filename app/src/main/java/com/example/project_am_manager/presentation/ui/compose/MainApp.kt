@file:OptIn(
    ExperimentalAnimationApi::class, ExperimentalAnimationApi::class,
    ExperimentalAnimationApi::class
)

package com.example.project_am_manager.presentation.ui.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.example.project_am_manager.presentation.ui.compose.appcontent.MainAppContent
import com.example.project_am_manager.presentation.viewmodel.MainViewModel
import com.example.project_am_manager.ui.theme.MainTheme

@Composable
fun MainApp(viewModel: MainViewModel) {
    MainTheme {
        MainAppContent(viewModel)
    }
}


