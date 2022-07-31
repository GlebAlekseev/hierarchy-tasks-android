@file:OptIn(ExperimentalMaterialApi::class)

package com.example.project_am_manager.presentation.viewmodel

import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.project_am_manager.domain.entity.BoardItem
import com.example.project_am_manager.domain.entity.TaskItem
import com.example.project_am_manager.domain.interactor.*
import com.example.project_am_manager.domain.repository.BoardListRepository
import com.example.project_am_manager.domain.repository.TaskListRepository
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import routing.Screen

@OptIn(ExperimentalPagerApi::class)
class MainViewModel(
    private val repositoryTask: TaskListRepository,
    private val repositoryBoard: BoardListRepository
) : ViewModel(),IGuaranteeViewModel {

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

    // MainApp

    private var _animateNavController: MutableStateFlow<NavHostController?> = MutableStateFlow(null)
    val animateNavController: StateFlow<NavHostController?>
        get() = _animateNavController

    fun setAnimateNavController(value: NavHostController) {
        _animateNavController.value = value
    }


    private val _pagerState = MutableStateFlow(PagerState())
    override val pagerState: StateFlow<PagerState>
        get() = _pagerState

    fun setPagerState(value: PagerState) {
        _pagerState.value = value
    }


    private var _screenStateMain: MutableStateFlow<Screen> = MutableStateFlow(Screen.Home)
    val screenStateMain: StateFlow<Screen>
        get() = _screenStateMain

    fun setScreenStateMain(value: Screen) {
        _screenStateMain.value = value
    }


    private var _selectedItem: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedItem: StateFlow<Int>
        get() = _selectedItem

    fun setSelectedItem(value: Int) {
        _selectedItem.value = value
    }


    // HomeScreen


    private val _parentBoardId = MutableStateFlow(1L)
    val parentBoardId: StateFlow<Long>
        get() = _parentBoardId

    fun setParentBoardId(value: Long) {
        _parentBoardId.value = value
    }


    private val _currentBoardId = MutableStateFlow(0L)
    val currentBoardId: StateFlow<Long>
        get() = _currentBoardId

    fun setCurrentBoardId(value: Long) {
        _currentBoardId.value = value
        setParentBoardId(getBoard(_currentBoardId.value).let { if (it != null) it.parent_id else 1 })
    }

    // HistoryScreen
    // HierarchyScreen

    val _scaleContent = MutableStateFlow(0.4f)
    val scaleContent: StateFlow<Float>
        get() = _scaleContent

    fun setScaleContent(value: Float) {
        _scaleContent.value = value
    }

    private var _offsetContent = MutableStateFlow(Offset(0f, 0f))
    val offsetContent: StateFlow<Offset>
        get() = _offsetContent

    fun setOffsetContent(value: Offset) {
        _offsetContent.value = value
    }


    private var _screenHierarchyState: MutableStateFlow<Screen> = MutableStateFlow(Screen.Hierarchy)
    val screenHierarchyState: StateFlow<Screen>
        get() = _screenHierarchyState

    fun setScreenHierarchyState(value: Screen) {
        _screenHierarchyState.value = value
    }


    private var _transformableState: MutableStateFlow<TransformableState> = MutableStateFlow(
        TransformableState(onTransformation = { zoomChange, offsetChange, rotationChange ->
            if (_scaleContent.value * zoomChange >= 0.1) {
                setScaleContent(_scaleContent.value * zoomChange)
                setOffsetContent(_offsetContent.value + offsetChange)
            }
        })
    )
    val transformableState: StateFlow<TransformableState>
        get() = _transformableState

    fun setTransformableState(value: TransformableState) {
        _transformableState.value = value
    }


    private val _openDialogAdding = MutableStateFlow(false)
    val openDialogAdding: StateFlow<Boolean>
        get() = _openDialogAdding

    fun setOpenDialogAdding(value: Boolean) {
        _openDialogAdding.value = value
    }


    private val _openDialogEditing = MutableStateFlow(false)
    val openDialogEditing: StateFlow<Boolean>
        get() = _openDialogEditing

    fun setOpenDialogEditing(value: Boolean) {
        _openDialogEditing.value = value
    }

    // AlertDialogAdding

    private val _nameBoardAlertDialogAdding = MutableStateFlow("")
    val nameBoardAlertDialogAdding: StateFlow<String>
        get() = _nameBoardAlertDialogAdding

    fun setNameBoardAlertDialogAdding(value: String) {
        _nameBoardAlertDialogAdding.value = value
    }


    // AlertDialogEditing

    private val _nameBoardAlertDialogEditing = MutableStateFlow("")
    val nameBoardAlertDialogEditing: StateFlow<String>
        get() = _nameBoardAlertDialogEditing

    fun setNameBoardAlertDialogEditing(value: String) {
        _nameBoardAlertDialogEditing.value = value
    }


    // ModalBottomSheet

    private val _stateModal = MutableStateFlow(ModalBottomSheetState(ModalBottomSheetValue.Hidden))
    override val stateModal: StateFlow<ModalBottomSheetState>
        get() = _stateModal

    private val _expandedBoardIdsList = MutableStateFlow(listOf<Long>())
    override val expandedBoardIdsList: StateFlow<List<Long>> get() = _expandedBoardIdsList
    override fun onBoardArrowClicked(boardId: Long) {
        _expandedBoardIdsList.value = _expandedBoardIdsList.value.toMutableList().also { list ->
            if (list.contains(boardId)) list.remove(boardId) else list.add(boardId)
        }
    }







    private val _indexAnimateTarget = MutableStateFlow(0)
    override val indexAnimateTarget: StateFlow<Int>
        get() {
//            refreshIndexAnimateTarget()
            return _indexAnimateTarget
        }
    fun setIndexAnimateTarget(value:Int){
        _indexAnimateTarget.value = value
    }
    fun refreshIndexAnimateTarget(){
        setIndexAnimateTarget(getBoardList().value?.filter { a ->
            (a.parent_id == _parentBoardId.value && !getTaskList().value?.filter { it.parent_id == a.id }
                .isNullOrEmpty())
        }!!.indexOf(
            if (getBoardList().value!!.filter { it.id == _currentBoardId.value }.lastOrNull() != null)
                getBoardList().value!!.filter { it.id == _currentBoardId.value }.lastOrNull()
            else
                BoardItem("", "", 0,0)
        ).let { if (it == -1) 0 else it })
        println("EEEEEEE refresh=${_indexAnimateTarget.value}")
    }















}