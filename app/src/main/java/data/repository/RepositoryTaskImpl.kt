package data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import data.database.dao.TaskDao
import data.database.dbmapper.DbMapperTask
import data.database.model.BoardDbModel
import data.database.model.TaskDbModel
import domain.model.BoardModel
import domain.model.TaskModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RepositoryTaskImpl(private val taskDao: TaskDao, private val mapper: DbMapperTask) : RepositoryTask {
    private val allTasks: MutableLiveData<List<TaskModel>> by lazy {
        MutableLiveData<List<TaskModel>>()
    }
    init {
        initDatabase(this::updateTaskLiveData)
    }
    private fun initDatabase(postInitAction: () -> Unit) {
        GlobalScope.launch {
            delay(300)

            val helperTasks = TaskDbModel.HELPER_TASKS

            if (taskDao.getAll().isEmpty()){
                taskDao.insertAll(*helperTasks)
            }
            postInitAction.invoke()
        }
    }


    private fun getAllTasksFromDatabase(): List<TaskModel> =
        taskDao.getAll().map(mapper::map)

    private fun updateTaskLiveData() {
        allTasks.postValue(getAllTasksFromDatabase())
    }

    override fun getAll(): LiveData<List<TaskModel>> = allTasks


    override fun insertAll(vararg TaskModels: TaskModel) {
        taskDao.insertAll(*TaskModels.map { mapper.mapDb(it) }.toTypedArray())
        updateTaskLiveData()
    }

    override fun updateAll(vararg TaskModels: TaskModel) {
        taskDao.updateAll(*TaskModels.map { mapper.mapDb(it) }.toTypedArray())
        updateTaskLiveData()
    }

    override fun deleteAll() {
        taskDao.deleteAll()
        updateTaskLiveData()
    }

    override fun get(id: Long): TaskModel? = allTasks.map { it.filter { it.id==id } }.value?.last()

    override fun insert(taskModel: TaskModel) {
        taskDao.insert(mapper.mapDb(taskModel))
        updateTaskLiveData()
    }

    override fun update(taskModel: TaskModel) {
        taskDao.update(mapper.mapDb(taskModel))
        updateTaskLiveData()
    }

    override fun delete(taskModel: TaskModel) {
        taskDao.delete(mapper.mapDb(taskModel))
        updateTaskLiveData()
    }

}