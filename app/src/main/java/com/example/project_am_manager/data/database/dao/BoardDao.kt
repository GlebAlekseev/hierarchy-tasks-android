package com.example.project_am_manager.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.project_am_manager.data.database.model.BoardDbModel


@Dao
interface BoardDao {
    @Query("SELECT * FROM BoardDbModel WHERE id = :id")
    fun get(id: Long): BoardDbModel?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(boardDbModel: BoardDbModel)
    @Update
    fun update(boardDbModel: BoardDbModel)
    @Delete
    fun delete(boardDbModel: BoardDbModel)
    @Query("SELECT * FROM BoardDbModel ORDER BY date ASC")
    fun getAll(): LiveData<List<BoardDbModel>>
}