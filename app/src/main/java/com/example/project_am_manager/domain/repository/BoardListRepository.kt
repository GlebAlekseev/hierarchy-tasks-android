package com.example.project_am_manager.domain.repository

import androidx.lifecycle.LiveData
import com.example.project_am_manager.domain.entity.BoardItem

interface BoardListRepository {
    fun addBoardItem(boardItem: BoardItem)
    fun deleteBoardItem(boardItem: BoardItem)
    fun editBoardItem(boardItem: BoardItem)
    fun getBoardItem(boardItemId: Long): BoardItem
    fun getBoardList(): LiveData<List<BoardItem>>
}