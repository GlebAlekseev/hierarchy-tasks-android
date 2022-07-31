package com.example.project_am_manager.data.repositoryImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.project_am_manager.data.database.dao.BoardDao
import com.example.project_am_manager.data.database.model.BoardDbModel
import com.example.project_am_manager.data.mapper.BoardMapperImpl
import com.example.project_am_manager.domain.entity.BoardItem
import com.example.project_am_manager.domain.mapper.Mapper
import com.example.project_am_manager.domain.repository.BoardListRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class BoardListRepositoryImpl @Inject constructor(private val boardListDao: BoardDao,private val mapper: Mapper<BoardItem,BoardDbModel>): BoardListRepository {
    init {
        initDatabase()
    }

    private fun initDatabase() {
        GlobalScope.launch {
            val rootBoard = BoardDbModel.ROOT_BOARD
            val helperBoard = BoardDbModel.HELPER_BOARD
            if (boardListDao.getAll().value?.isEmpty() == true){
                boardListDao.insert(rootBoard)
                boardListDao.insert(helperBoard)
            }
        }
    }

    override fun addBoardItem(boardItem: BoardItem) {
        boardListDao.insert(mapper.mapItemToDbModel(boardItem))
    }

    override fun deleteBoardItem(boardItem: BoardItem) {
        boardListDao.delete(mapper.mapItemToDbModel(boardItem))
    }

    override fun editBoardItem(boardItem: BoardItem) {
        boardListDao.update(mapper.mapItemToDbModel(boardItem))
    }

    override fun getBoardItem(boardItemId: Long): BoardItem {
        return mapper.mapDbModelToItem(boardListDao.get(boardItemId))
    }

    override fun getBoardList(): LiveData<List<BoardItem>> = Transformations.map(boardListDao.getAll()){
        it.map { item->
            mapper.mapDbModelToItem(item)
        }
    }
}