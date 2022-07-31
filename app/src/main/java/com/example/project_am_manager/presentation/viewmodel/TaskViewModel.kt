@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalPagerApi::class
)

package com.example.project_am_manager.presentation.viewmodel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.LiveData
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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
class TaskViewModel(private val repositoryTask: TaskListRepository, private val repositoryBoard: BoardListRepository) : ViewModel(),IGuaranteeViewModel{

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



    private val _currentDate = MutableStateFlow(SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()))
    val currentDate: StateFlow<String>
        get() = _currentDate
    fun setCurrentDate(value:String){
        _currentDate.value = value
    }

    private val _descriptionTextFieldEdit = MutableStateFlow("")
    val descriptionTextFieldEdit: StateFlow<String>
        get() = _descriptionTextFieldEdit
    fun setDescriptionTextFieldEdit(value:String){
        _descriptionTextFieldEdit.value = value
    }

    private val _nameTextFieldEdit = MutableStateFlow("")
    val nameTextFieldEdit: StateFlow<String>
        get() = _nameTextFieldEdit
    fun setNameTextFieldEdit(value:String){
        _nameTextFieldEdit.value = value
    }

    private val _transmittedParentId = MutableStateFlow(0L)
    val transmittedParentId: StateFlow<Long>
        get() = _transmittedParentId
    fun setTransmittedParentId(value:Long){
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
    fun setOpenDialogSave(value:Boolean){
        _openDialogSave.value = value
    }

    // TaskActivity

    private val _transmittedId = MutableStateFlow(0L)
    val transmittedId: StateFlow<Long>
        get() = _transmittedId
    fun setTransmittedId(value:Long){
        _transmittedId.value = value
        if (value != 0L){
            val currentTask = getTaskList().value?.filter { it.id == value }?.firstOrNull()
            _nameTextFieldEdit.value = currentTask.let { if (it != null) it.name else  "error"}
            _descriptionTextFieldEdit.value = currentTask.let { if (it != null) it.description else  "error"}
            _currentDate.value = currentTask.let { if (it != null) it.date else  "error"}
            _transmittedParentId.value = currentTask.let { if (it != null) it.parent_id else  0L}
        }
    }




    private val _expandedBoardIdsList = MutableStateFlow(listOf<Long>())
    override val expandedBoardIdsList: StateFlow<List<Long>> get() = _expandedBoardIdsList
    override fun onBoardArrowClicked(boardId: Long) {
        _expandedBoardIdsList.value = _expandedBoardIdsList.value.toMutableList().also { list ->
            if (list.contains(boardId)) list.remove(boardId) else list.add(boardId)
        }
    }




    // mock
    override val indexAnimateTarget: StateFlow<Int>
        get() = MutableStateFlow(0)

    override val pagerState: StateFlow<PagerState>
        get() = MutableStateFlow(PagerState(0))

//
//    private val _parentBoardId = MutableStateFlow(1L)
//    val parentBoardId: StateFlow<Long>
//        get() = _parentBoardId
//    fun setParentBoardId(value:Long){
//        _parentBoardId.value = value
//    }
//
//

//
//
//
//    @OptIn(ExperimentalMaterialApi::class)
//    private var _screenStateMain:MutableStateFlow<Screen> = MutableStateFlow(Screen.Home)
//    @OptIn(ExperimentalMaterialApi::class)
//    val screenStateMain: StateFlow<Screen>
//        get() = _screenStateMain
//    fun setScreenStateMain(value:Screen){
//        _screenStateMain.value = value
//    }
//
//

}