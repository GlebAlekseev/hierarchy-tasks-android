package com.example.project_am_manager.domain.interactor

import com.example.project_am_manager.domain.entity.BoardItem
import com.example.project_am_manager.domain.repository.BoardListRepository

class GetBoardItemUseCase(private val boardListRepository: BoardListRepository) {
    operator fun invoke(boardItemId: Long): BoardItem  =  boardListRepository.getBoardItem(boardItemId)
}