package com.example.project_am_manager.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.project_am_manager.data.database.model.TaskDbModel


@Dao
interface TaskDao {
    @Query("SELECT * FROM TaskDbModel WHERE id = :id")
    fun get(id: Long): TaskDbModel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskDbModel: TaskDbModel)
    @Update
    fun update(taskDbModel: TaskDbModel)
    @Delete
    fun delete(taskDbModel: TaskDbModel)
    @Query("SELECT * FROM TaskDbModel ORDER BY date ASC")
    fun getAll(): LiveData<List<TaskDbModel>>
}