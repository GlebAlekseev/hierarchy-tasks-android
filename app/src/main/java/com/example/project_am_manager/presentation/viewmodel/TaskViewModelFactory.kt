package com.example.project_am_manager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project_am_manager.domain.repository.BoardListRepository
import com.example.project_am_manager.domain.repository.TaskListRepository


class TaskViewModelFactory(
    private val taskListRepository: TaskListRepository,
    private val boardListRepository: BoardListRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(taskListRepository,boardListRepository) as T
    }
}