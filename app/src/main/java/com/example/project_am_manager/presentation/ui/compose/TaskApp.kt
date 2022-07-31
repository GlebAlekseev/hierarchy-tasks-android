package com.example.project_am_manager

import androidx.compose.animation.Crossfade
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.project_am_manager.domain.entity.TaskItem
import com.example.project_am_manager.presentation.activity.TaskActivity
import com.example.project_am_manager.presentation.ui.compose.components.AlertDialogSave
import com.example.project_am_manager.presentation.ui.compose.components.modalbottomsheet.ModalBottomSheetChoose
import com.example.project_am_manager.presentation.ui.compose.components.topbar.TopTaskAppBar
import com.example.project_am_manager.presentation.ui.compose.routing.BackButtonAction
import com.example.project_am_manager.presentation.viewmodel.TaskViewModel
import com.example.project_am_manager.ui.theme.MainTheme
import routing.ScreenTask
import routing.TaskRouter

@Composable
fun TaskApp(viewModel: TaskViewModel) {
    MainTheme {
        AppContent(viewModel)
    }
}

@Composable
fun AppContent(viewModel: TaskViewModel) {

    Crossfade(targetState = TaskRouter.currentScreen) { screenState: MutableState<ScreenTask> ->
        Scaffold(
            topBar = { TopTaskAppBar(screenState, viewModel) },
            content = { MainScreenTaskContainer(screenState, viewModel) }
        )
    }

    ModalBottomSheetChoose(viewModel, true)

    val transmittedId by viewModel.transmittedId.collectAsState()
    val descriptionTextFieldEdit by viewModel.descriptionTextFieldEdit.collectAsState()
    val nameTextFieldEdit by viewModel.nameTextFieldEdit.collectAsState()
    val transmittedParentId by viewModel.transmittedParentId.collectAsState()

    AlertDialogSave(viewModel)
    val allTasks: List<TaskItem> by viewModel.getTaskList().observeAsState(emptyList())
    val context = LocalContext.current as TaskActivity

    BackButtonAction {
        val name_descr_parent_db = allTasks.filter { it.id == transmittedId }.lastOrNull()
            .let { if (it != null) it.name + it.description + it.parent_id else "" }
        if (transmittedId == 0L && nameTextFieldEdit.length == 0 && descriptionTextFieldEdit.length == 0) {
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

@Composable
fun MainScreenTaskContainer(
    screenState: MutableState<ScreenTask>,
    viewModel: TaskViewModel
) {
    when (screenState.value) {
        ScreenTask.Edit -> EditScreen(viewModel)
        ScreenTask.View -> ViewScreen(viewModel)
    }
}
