package com.example.project_am_manager.presentation.ui.compose.appcontent

import androidx.compose.animation.Crossfade
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.project_am_manager.presentation.activity.TaskActivity
import com.example.project_am_manager.presentation.ui.compose.alertdialog.TaskAlertDialogBack
import com.example.project_am_manager.presentation.ui.compose.content.TaskContent
import com.example.project_am_manager.presentation.ui.compose.modalbottomsheet.ModalBottomSheetChooseBoard
import com.example.project_am_manager.presentation.ui.compose.routing.BackButtonAction
import com.example.project_am_manager.presentation.ui.compose.topbar.TaskTopBar
import com.example.project_am_manager.presentation.viewmodel.TaskViewModel
import routing.TaskRouter
import routing.TaskScreen

@Composable
fun TaskAppContent(viewModel: TaskViewModel) {
    Crossfade(targetState = TaskRouter.currentScreen) { screenState: MutableState<TaskScreen> ->
        viewModel.setScreenState(screenState.value)
        Scaffold(
            topBar = { TaskTopBar(viewModel) },
            content = { TaskContent(viewModel) }
        )
    }
    ModalBottomSheetChooseBoard(viewModel, true)
    TaskAlertDialogBack(viewModel)
    SetBackButtonAction(viewModel)
}

@Composable
fun SetBackButtonAction(viewModel: TaskViewModel) {
    val transmittedId by viewModel.transmittedId.collectAsState()
    val descriptionTextFieldEdit by viewModel.descriptionTextFieldEdit.collectAsState()
    val nameTextFieldEdit by viewModel.nameTextFieldEdit.collectAsState()
    val transmittedParentId by viewModel.transmittedParentId.collectAsState()
    val context = LocalContext.current as TaskActivity

    BackButtonAction {
        val name_descr_parent_db = if (transmittedId != 0L) with(viewModel.getTask(transmittedId)) {
            name + description + parent_id.toString()
        } else transmittedParentId.toString()
        if (transmittedId == 0L && nameTextFieldEdit.isEmpty() && descriptionTextFieldEdit.isEmpty()) {
            viewModel.setOpenDialogSave(false)
            context.finish()
        } else if (name_descr_parent_db == nameTextFieldEdit + descriptionTextFieldEdit + transmittedParentId) {
            viewModel.setOpenDialogSave(false)
            context.finish()
        } else {
            viewModel.setOpenDialogSave(true)
        }
    }
}