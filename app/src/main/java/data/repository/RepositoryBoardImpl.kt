package data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import data.database.dao.BoardDao
import data.database.dbmapper.DbMapperBoard
import data.database.model.BoardDbModel
import domain.model.BoardModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RepositoryBoardImpl(private val boardDao: BoardDao, private val mapper: DbMapperBoard) : RepositoryBoard {
    private val allBoards: MutableLiveData<List<BoardModel>> by lazy {
        MutableLiveData<List<BoardModel>>()
    }
    init {
        initDatabase(this::updateBoardLiveData)
    }
    private fun initDatabase(postInitAction: () -> Unit) {
        GlobalScope.launch {
            val rootBoard = BoardDbModel.ROOT_BOARD
            val helperBoard = BoardDbModel.HELPER_BOARD

            if (boardDao.getAll().isEmpty()){
                boardDao.insert(rootBoard)
                boardDao.insert(helperBoard)
            }
            postInitAction.invoke()

        }
    }



    private fun getAllBoardsFromDatabase(): List<BoardModel> =
        boardDao.getAll().map(mapper::map)

    private fun updateBoardLiveData() {
        Log.d("UPDateBoardLiveData", getAllBoardsFromDatabase().toString())
        allBoards.postValue(getAllBoardsFromDatabase())
    }

    override fun getAll(): LiveData<List<BoardModel>> {
        Log.d("RPP", allBoards.value.toString())
        return  allBoards
    }


    override fun insertAll(vararg BoardModels: BoardModel) {
        boardDao.insertAll(*BoardModels.map { mapper.mapDb(it) }.toTypedArray())
        updateBoardLiveData()
    }

    override fun updateAll(vararg BoardModels: BoardModel) {
        boardDao.updateAll(*BoardModels.map { mapper.mapDb(it) }.toTypedArray())
        updateBoardLiveData()
    }

    override fun deleteAll() {
        boardDao.deleteAll()
        updateBoardLiveData()
    }

    override fun get(id: Long): BoardModel? = allBoards.map { it.filter { it.id==id } }.value?.last()

    override fun insert(boardModel: BoardModel) {
        boardDao.insert(mapper.mapDb(boardModel))
        updateBoardLiveData()
    }

    override fun update(boardModel: BoardModel) {
        boardDao.update(mapper.mapDb(boardModel))
        updateBoardLiveData()
    }

    override fun delete(boardModel: BoardModel) {
        boardDao.delete(mapper.mapDb(boardModel))
        updateBoardLiveData()
    }
}