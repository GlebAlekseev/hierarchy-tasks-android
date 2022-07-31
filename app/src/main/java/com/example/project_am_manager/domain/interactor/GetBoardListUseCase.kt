package com.example.project_am_manager.domain.interactor

import androidx.lifecycle.LiveData
import com.example.project_am_manager.domain.entity.BoardItem
import com.example.project_am_manager.domain.repository.BoardListRepository

class GetBoardListUseCase(private val boardListRepository: BoardListRepository) {
    operator fun invoke(): LiveData<List<BoardItem>> = boardListRepository.getBoardList()
}