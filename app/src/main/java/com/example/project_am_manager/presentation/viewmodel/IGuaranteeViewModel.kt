@file:OptIn(ExperimentalPagerApi::class)

package com.example.project_am_manager.presentation.viewmodel

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.lifecycle.LiveData
import com.example.project_am_manager.domain.entity.BoardItem
import com.example.project_am_manager.domain.entity.TaskItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterialApi::class)
interface IGuaranteeViewModel {

    fun addBoard(item: BoardItem)
    fun addTask(item: TaskItem)

    fun deleteBoard(item: BoardItem)
    fun deleteTask(item: TaskItem)

    fun editBoard(item: BoardItem)
    fun editTask(item: TaskItem)

    fun getBoard(id: Long): BoardItem
    fun getTask(id: Long): TaskItem

    fun getBoardList(): LiveData<List<BoardItem>>
    fun getTaskList(): LiveData<List<TaskItem>>

    val expandedBoardIdsList: StateFlow<List<Long>>
    fun onBoardArrowClicked(boardId: Long)

    val stateModal: StateFlow<ModalBottomSheetState>
    val indexAnimateTarget: StateFlow<Int>
    val pagerState: StateFlow<PagerState>
}