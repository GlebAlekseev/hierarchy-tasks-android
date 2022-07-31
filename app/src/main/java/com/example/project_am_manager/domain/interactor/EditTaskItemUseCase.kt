package com.example.project_am_manager.domain.interactor

import com.example.project_am_manager.domain.entity.TaskItem
import com.example.project_am_manager.domain.repository.TaskListRepository

class EditTaskItemUseCase(private val taskListRepository: TaskListRepository) {
    operator fun invoke(taskItem: TaskItem) = taskListRepository.editTaskItem(taskItem)
}