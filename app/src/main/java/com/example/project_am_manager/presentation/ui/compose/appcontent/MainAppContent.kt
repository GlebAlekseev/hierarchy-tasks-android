package com.example.project_am_manager.presentation.ui.compose.appcontent

import androidx.compose.animation.Crossfade
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.project_am_manager.presentation.ui.compose.alertdialog.MainAlertDialogAdding
import com.example.project_am_manager.presentation.ui.compose.alertdialog.MainAlertDialogEditing
import com.example.project_am_manager.presentation.ui.compose.bottombar.MainBottomBar
import com.example.project_am_manager.presentation.ui.compose.content.MainContent
import com.example.project_am_manager.presentation.ui.compose.floatingactionbutton.main.MainExtendedFloatingActionButton
import com.example.project_am_manager.presentation.ui.compose.modalbottomsheet.ModalBottomSheetChooseBoard
import com.example.project_am_manager.presentation.viewmodel.MainViewModel
import routing.MainRouter
import routing.MainScreen


@Composable
fun MainAppContent(viewModel: MainViewModel) {
    Crossfade(targetState = MainRouter.currentScreen) { screenState: MutableState<MainScreen> ->
        viewModel.setScreenState(screenState.value)
        Scaffold(
            content = { MainContent(viewModel) },
            bottomBar = { MainBottomBar(viewModel) },
            floatingActionButton = { MainExtendedFloatingActionButton(viewModel) }
        )
    }
    ModalBottomSheetChooseBoard(viewModel)
    MainAlertDialogAdding(viewModel)
    MainAlertDialogEditing(viewModel)
}

