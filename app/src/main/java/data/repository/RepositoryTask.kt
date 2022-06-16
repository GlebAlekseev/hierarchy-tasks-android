package data.repository

import androidx.lifecycle.LiveData
import domain.model.BoardModel
import domain.model.TaskModel

interface RepositoryTask {

    fun getAll(): LiveData<List<TaskModel>>
    fun insertAll(vararg TaskModels: TaskModel)
    fun updateAll(vararg TaskModels: TaskModel)
    fun deleteAll()

    fun get(id: Long): TaskModel?
    fun insert(taskModel: TaskModel)
    fun update(taskModel: TaskModel)
    fun delete(taskModel: TaskModel)
}