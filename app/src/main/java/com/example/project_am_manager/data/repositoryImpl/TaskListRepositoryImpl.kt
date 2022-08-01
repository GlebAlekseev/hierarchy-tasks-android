package com.example.project_am_manager.data.repositoryImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.project_am_manager.data.database.dao.TaskDao
import com.example.project_am_manager.data.database.model.TaskDbModel
import com.example.project_am_manager.domain.entity.TaskItem
import com.example.project_am_manager.domain.mapper.Mapper
import com.example.project_am_manager.domain.repository.TaskListRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject


class TaskListRepositoryImpl @Inject constructor(
    private val taskListDao: TaskDao,
    private val mapper: Mapper<TaskItem, TaskDbModel>
) : TaskListRepository {
    init {
        initDatabase()
    }

    private fun initDatabase() {
        taskListDao.getAll().observeForever { list ->
            GlobalScope.launch {
                delay(300)
                val helperTasks = TaskDbModel.HELPER_TASKS
                if (list.isEmpty()) {
                    helperTasks.forEach { taskListDao.insert(it) }
                }
            }
        }
    }

    override fun addTaskItem(taskItem: TaskItem) {
        taskListDao.insert(mapper.mapItemToDbModel(taskItem))
    }

    override fun deleteTaskItem(taskItem: TaskItem) {
        taskListDao.delete(mapper.mapItemToDbModel(taskItem))
    }

    override fun editTaskItem(taskItem: TaskItem) {
        taskListDao.update(mapper.mapItemToDbModel(taskItem))
    }

    override fun getTaskItem(taskItemId: Long): TaskItem {
        return mapper.mapDbModelToItem(taskListDao.get(taskItemId) ?: throw RuntimeException("taskItemId: $taskItemId not exist"))
    }

    override fun getTaskList(): LiveData<List<TaskItem>> =
        Transformations.map(taskListDao.getAll()) {
            it.map { item ->
                mapper.mapDbModelToItem(item)
            }
        }
}