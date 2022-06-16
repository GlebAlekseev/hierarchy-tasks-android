package data.database.dao

import androidx.room.*
import data.database.model.BoardDbModel


@Dao
interface BoardDao {

    @Query("SELECT * FROM BoardDbModel ORDER BY date ASC")
    fun getAll(): List<BoardDbModel>
    @Insert
    fun insertAll(vararg BoardDbModels: BoardDbModel)
    @Update
    fun updateAll(vararg BoardDbModels: BoardDbModel)
    @Query("DELETE FROM BoardDbModel")
    fun deleteAll()


    @Query("SELECT * FROM BoardDbModel WHERE id = :id")
    fun get(id: Long): BoardDbModel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(boardDbModel: BoardDbModel)
    @Update
    fun update(boardDbModel: BoardDbModel)
    @Delete
    fun delete(boardDbModel: BoardDbModel)


}