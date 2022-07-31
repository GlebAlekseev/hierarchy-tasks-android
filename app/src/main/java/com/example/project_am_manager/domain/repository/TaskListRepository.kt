package com.example.project_am_manager.domain.repository

import androidx.lifecycle.LiveData
import com.example.project_am_manager.domain.entity.TaskItem

interface TaskListRepository {
    fun addTaskItem(taskItem: TaskItem)
    fun deleteTaskItem(taskItem: TaskItem)
    fun editTaskItem(taskItem: TaskItem)
    fun getTaskItem(taskItemId: Long): TaskItem
    fun getTaskList(): LiveData<List<TaskItem>>
}