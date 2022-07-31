package com.example.project_am_manager.domain.interactor

import androidx.lifecycle.LiveData
import com.example.project_am_manager.domain.entity.TaskItem
import com.example.project_am_manager.domain.repository.TaskListRepository

class GetTaskListUseCase(private val taskListRepository: TaskListRepository) {
    operator fun invoke(): LiveData<List<TaskItem>> = taskListRepository.getTaskList()
}