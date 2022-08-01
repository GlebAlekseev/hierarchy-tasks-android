@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalPagerApi::class, ExperimentalFoundationApi::class
)

package com.example.project_am_manager.presentation.viewmodel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.project_am_manager.domain.entity.BoardItem
import com.example.project_am_manager.domain.entity.TaskItem
import com.example.project_am_manager.domain.interactor.*
import com.example.project_am_manager.domain.repository.BoardListRepository
import com.example.project_am_manager.domain.repository.TaskListRepository
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import routing.TaskScreen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
class TaskViewModel(
    private val repositoryTask: TaskListRepository,
    private val repositoryBoard: BoardListRepository,
) : ViewModel(), IGuaranteeViewModel {

    private val addBoardItemUseCase = AddBoardItemUseCase(repositoryBoard)
    private val addTaskItemUseCase = AddTaskItemUseCase(repositoryTask)

    private val deleteBoardItemUseCase = DeleteBoardItemUseCase(repositoryBoard)
    private val deleteTaskItemUseCase = DeleteTaskItemUseCase(repositoryTask)

    private val editBoardItemUseCase = EditBoardItemUseCase(repositoryBoard)
    private val editTaskItemUseCase = EditTaskItemUseCase(repositoryTask)

    private val getBoardItemUseCase = GetBoardItemUseCase(repositoryBoard)
    private val getTaskItemUseCase = GetTaskItemUseCase(repositoryTask)

    private val getBoardListUseCase = GetBoardListUseCase(repositoryBoard)
    private val getTaskListUseCase = GetTaskListUseCase(repositoryTask)

    override fun addBoard(item: BoardItem) {
        addBoardItemUseCase(item)
    }

    override fun addTask(item: TaskItem) {
        addTaskItemUseCase(item)
    }

    override fun deleteBoard(item: BoardItem) {
        deleteBoardItemUseCase(item)
    }

    override fun deleteTask(item: TaskItem) {
        deleteTaskItemUseCase(item)
    }

    override fun editBoard(item: BoardItem) {
        editBoardItemUseCase(item)
    }

    override fun editTask(item: TaskItem) {
        editTaskItemUseCase(item)
    }

    override fun getBoard(id: Long): BoardItem {
        return getBoardItemUseCase(id)
    }

    override fun getTask(id: Long): TaskItem {
        return getTaskItemUseCase(id)
    }

    override fun getBoardList(): LiveData<List<BoardItem>> {
        return getBoardListUseCase()
    }

    override fun getTaskList(): LiveData<List<TaskItem>> {
        return getTaskListUseCase()
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

// ViewScreen

    private val _currentDate =
        MutableStateFlow(SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()))
    val currentDate: StateFlow<String>
        get() = _currentDate

    fun setCurrentDate(value: String) {
        _currentDate.value = value
    }

    private val _descriptionTextFieldEdit = MutableStateFlow("")
    val descriptionTextFieldEdit: StateFlow<String>
        get() = _descriptionTextFieldEdit

    fun setDescriptionTextFieldEdit(value: String) {
        _descriptionTextFieldEdit.value = value
    }

    private val _nameTextFieldEdit = MutableStateFlow("")
    val nameTextFieldEdit: StateFlow<String>
        get() = _nameTextFieldEdit

    fun setNameTextFieldEdit(value: String) {
        _nameTextFieldEdit.value = value
    }

    private val _transmittedParentId = MutableStateFlow(0L)
    val transmittedParentId: StateFlow<Long>
        get() = _transmittedParentId

    fun setTransmittedParentId(value: Long) {
        _transmittedParentId.value = value
    }

// EditScreen


    private val _stateModal = MutableStateFlow(ModalBottomSheetState(ModalBottomSheetValue.Hidden))
    override val stateModal: StateFlow<ModalBottomSheetState>
        get() = _stateModal


    private val _relocationRequester = MutableStateFlow(BringIntoViewRequester())
    val relocationRequester: StateFlow<BringIntoViewRequester>
        get() = _relocationRequester


    // AlertDialogSave


    private val _openDialogSave = MutableStateFlow(false)
    val openDialogSave: StateFlow<Boolean>
        get() = _openDialogSave

    fun setOpenDialogSave(value: Boolean) {
        _openDialogSave.value = value
    }

    // TaskActivity

    private val _transmittedId = MutableStateFlow(0L)
    val transmittedId: StateFlow<Long>
        get() = _transmittedId

    fun setTransmittedId(value: Long) {
        _transmittedId.value = value
        val currentTask = getTask(value)
        _nameTextFieldEdit.value = currentTask.name
        _descriptionTextFieldEdit.value = currentTask.description
        _currentDate.value = currentTask.date
//            _transmittedParentId.value = currentTask.let { if (it != null) it.parent_id else  0L}

    }


    private val _expandedBoardIdsList = MutableStateFlow(listOf<Long>())
    override val expandedBoardIdsList: StateFlow<List<Long>> get() = _expandedBoardIdsList
    override fun onBoardArrowClicked(boardId: Long) {
        _expandedBoardIdsList.value = _expandedBoardIdsList.value.toMutableList().also { list ->
            if (list.contains(boardId)) list.remove(boardId) else list.add(boardId)
        }
    }

    // TaskAppContent

    private var _screenState: MutableStateFlow<TaskScreen> = MutableStateFlow(TaskScreen.View)
    val screenState: StateFlow<TaskScreen>
        get() = _screenState

    fun setScreenState(value: TaskScreen) {
        _screenState.value = value
    }


    // mock
    override val indexAnimateTarget: StateFlow<Int>
        get() = MutableStateFlow(0)

    override val pagerState: StateFlow<PagerState>
        get() = MutableStateFlow(PagerState(0))


    ////////////////////
    fun getTasksForParent(parentId: Long): LiveData<List<TaskItem>> =
        Transformations.map(getTaskList()) {
            it.filter { item ->
                item.parent_id == parentId
            }
        }

    override fun getBoardsForParent(parentId: Long): LiveData<List<BoardItem>> =
        Transformations.map(getBoardList()) {
            it.filter { item ->
                item.parent_id == parentId
            }
        }

    override fun getBoardsForParentWithChildrenHaveTasks(parentId: Long): LiveData<List<BoardItem>> =
        MediatorLiveData<List<BoardItem>>().apply {
            addSource(getTaskList()) { tasklist ->
                addSource(getBoardsForParent(parentId)) { boardlist ->
                    value = boardlist.filter { item ->
                        item.parent_id == parentId && (isBoardHaveTasks(item,
                            tasklist) || item.id == parentId)
                    }
                }
            }
        }

    private fun isBoardParent(boardItem: BoardItem, boardList: List<BoardItem>): Boolean {
        return !boardList.filter { it.parent_id == boardItem.id }.isNullOrEmpty()
    }

    private fun isBoardHaveTasks(boardItem: BoardItem, taskList: List<TaskItem>): Boolean {
        return !taskList.filter { it.parent_id == boardItem.id }.isNullOrEmpty()
    }

    override fun getBoardsIsParent(): LiveData<List<BoardItem>> =
        Transformations.map(getBoardList()) {
            it.filter { item ->
                isBoardParent(item, it)
            }
        }

    override fun getBoardsWithChildrenHaveTasks(): LiveData<List<BoardItem>> =
        MediatorLiveData<List<BoardItem>>().apply {
            addSource(getTaskList()) { tasklist ->
                addSource(getBoardsIsParent()) { boardlist ->
                    value = boardlist.filter { item ->
                        isBoardHaveTasks(item, tasklist)
                    }
                }
            }
        }
}