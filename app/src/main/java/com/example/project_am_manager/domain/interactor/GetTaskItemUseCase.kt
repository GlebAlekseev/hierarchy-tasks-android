package com.example.project_am_manager.domain.interactor

import com.example.project_am_manager.domain.repository.TaskListRepository

class GetTaskItemUseCase(private val taskListRepository: TaskListRepository) {
    operator fun invoke(taskItemId: Long) = taskListRepository.getTaskItem(taskItemId)
}