package data.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import data.database.model.BoardDbModel
import domain.model.BoardModel

interface RepositoryBoard {
    fun getAll(): LiveData<List<BoardModel>>
    fun insertAll(vararg BoardModels: BoardModel)
    fun updateAll(vararg BoardModels: BoardModel)
    fun deleteAll()


    fun get(id: Long): BoardModel?
    fun insert(boardModel: BoardModel)
    fun update(boardModel: BoardModel)
    fun delete(boardModel: BoardModel)
}