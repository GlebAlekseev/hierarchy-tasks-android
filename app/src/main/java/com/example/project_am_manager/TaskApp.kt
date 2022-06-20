package com.example.project_am_manager

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.ui.theme.Project_AM_ManagerTheme
import com.skyyo.expandablelist.cards.BoardsScreen
import components.AlertDialogSave
import components.modalbottomsheet.ModalBottomSheetChoose
import components.topbar.TopTaskAppBar
import domain.model.TaskModel
import kotlinx.coroutines.*
import routing.BackButtonAction
import routing.ScreenTask
import routing.TaskRouter
import viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaskApp(viewModel: MainViewModel) {
    Project_AM_ManagerTheme{
        TaskContent(viewModel)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskContent(viewModel: MainViewModel){

    Crossfade(targetState = TaskRouter.currentScreen) { screenState: MutableState<ScreenTask> ->
        Scaffold(
            topBar = { TopTaskAppBar(screenState,viewModel) },
            content = { MainScreenTaskContainer(screenState,viewModel) }
        )
    }

    ModalBottomSheetChoose(viewModel,true)

    val transmittedId by viewModel.transmittedId.collectAsState()
    val descriptionTextFieldEdit by viewModel.descriptionTextFieldEdit.collectAsState()
    val nameTextFieldEdit by viewModel.nameTextFieldEdit.collectAsState()
    val transmittedParentId by viewModel.transmittedParentId .collectAsState()

    AlertDialogSave(viewModel)
    val allTasks: List<TaskModel> by viewModel.allTasks.observeAsState(emptyList())
    val context = LocalContext.current as TaskActivity

    BackButtonAction {
        val name_descr_parent_db = allTasks.filter { it.id == transmittedId }.lastOrNull().let { if (it != null) it.name + it.description + it.parent_id else "" }
        if (transmittedId == 0L && nameTextFieldEdit.length == 0 && descriptionTextFieldEdit.length == 0 ){
            viewModel.setOpenDialogSave(false)
            context.finish()
        }else if (name_descr_parent_db == nameTextFieldEdit + descriptionTextFieldEdit + transmittedParentId){
            viewModel.setOpenDialogSave(false)
            context.finish()
        }else{
            viewModel.setOpenDialogSave(true)
        }
    }
}


@Composable
fun MainScreenTaskContainer(
    screenState: MutableState<ScreenTask>,
    viewModel: MainViewModel
){
    when (screenState.value) {
        ScreenTask.Edit -> EditScreen(viewModel)
        ScreenTask.View -> ViewScreen(viewModel)
    }
}





