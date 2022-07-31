package com.example.project_am_manager.domain.interactor

import com.example.project_am_manager.domain.entity.BoardItem
import com.example.project_am_manager.domain.repository.BoardListRepository

class AddBoardItemUseCase(private val boardListRepository: BoardListRepository) {
    operator fun invoke(boardItem: BoardItem) = boardListRepository.addBoardItem(boardItem)
}