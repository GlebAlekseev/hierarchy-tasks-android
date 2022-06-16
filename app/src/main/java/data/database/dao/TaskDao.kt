package data.database.dao

import androidx.room.*
import data.database.model.TaskDbModel


@Dao
interface TaskDao {

    @Query("SELECT * FROM TaskDbModel ORDER BY date ASC")
    fun getAll(): List<TaskDbModel>
    @Insert
    fun insertAll(vararg TaskDbModels: TaskDbModel)
    @Update
    fun updateAll(vararg TaskDbModels: TaskDbModel)
    @Query("DELETE FROM TaskDbModel")
    fun deleteAll()


    @Query("SELECT * FROM TaskDbModel WHERE id = :id")
    fun get(id: String): List<TaskDbModel>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskDbModel: TaskDbModel)
    @Update
    fun update(taskDbModel: TaskDbModel)
    @Delete
    fun delete(taskDbModel: TaskDbModel)


}