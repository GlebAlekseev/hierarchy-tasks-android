package com.example.project_am_manager.presentation.ui.compose.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.project_am_manager.TaskEditScreen
import com.example.project_am_manager.TaskViewScreen
import com.example.project_am_manager.presentation.viewmodel.TaskViewModel
import routing.TaskScreen

@Composable
fun TaskContent(
    viewModel: TaskViewModel
) {
    val screenState by viewModel.screenState.collectAsState()
    when (screenState) {
        TaskScreen.Edit -> TaskEditScreen(viewModel)
        TaskScreen.View -> TaskViewScreen(viewModel)
    }
}
