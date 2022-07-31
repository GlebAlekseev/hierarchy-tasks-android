package com.example.project_am_manager.domain.interactor

import com.example.project_am_manager.domain.entity.TaskItem
import com.example.project_am_manager.domain.repository.TaskListRepository

class DeleteTaskItemUseCase(private val taskListRepository: TaskListRepository) {
    operator fun invoke(taskItem: TaskItem) = taskListRepository.deleteTaskItem(taskItem)
}