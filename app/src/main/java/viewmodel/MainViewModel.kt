package viewmodel

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.material.*
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.project_am_manager.MainActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.skyyo.expandablelist.cards.ExpandableCardModel
import data.repository.RepositoryBoard
import data.repository.RepositoryTask
import domain.model.BoardModel
import domain.model.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import routing.Screen
import routing.TManagerRouter
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(private val repositoryTask: RepositoryTask,private val repositoryBoard: RepositoryBoard) : ViewModel(){


    // Добавить, Удалить, Редактирвовать, Поиск, Получить по id, Получить все
    val allBoards by lazy { repositoryBoard.getAll()   }


    fun getBoard(id: Long): LiveData<List<BoardModel>> {
        return MutableLiveData<List<BoardModel>>(allBoards.value?.filter { it.id==id })
    }

    fun deleteBoard(board: BoardModel){
        viewModelScope.launch{
            repositoryBoard.delete(board)
        }
    }

    fun insertBoard(board: BoardModel){
        viewModelScope.launch{
            repositoryBoard.insert(board)
        }
    }

    fun updateBoard(board: BoardModel){
        viewModelScope.launch{
            repositoryBoard.update(board)
        }
    }



    // Добавить, Удалить, Редактирвовать, Поиск, Получить по id, Получить все
    val allTasks by lazy { repositoryTask.getAll() }


    fun getTask(id: Long): LiveData<TaskModel> {
        return MutableLiveData<TaskModel>(allTasks.value?.filter { it.id==id }?.last())
    }

    fun deleteTask(task: TaskModel){
        viewModelScope.launch{
            repositoryTask.delete(task)
        }
    }

    fun insertTask(task: TaskModel){
        viewModelScope.launch{
            repositoryTask.insert(task)
        }
    }

    fun updateTask(task: TaskModel){
        viewModelScope.launch{
            repositoryTask.update(task)
        }
    }



    private val _expandedBoardIdsList = MutableStateFlow(listOf<Long>())
    val expandedBoardIdsList: StateFlow<List<Long>> get() = _expandedBoardIdsList


    fun onBoardArrowClicked(boardId: Long) {
        _expandedBoardIdsList.value = _expandedBoardIdsList.value.toMutableList().also { list ->
            if (list.contains(boardId)) list.remove(boardId) else list.add(boardId)
        }
    }

    private val _openDialogSave = MutableStateFlow(false)
    val openDialogSave: StateFlow<Boolean>
        get() = _openDialogSave
    fun setOpenDialogSave(value:Boolean){
        _openDialogSave.value = value
    }

    private val _transmittedId = MutableStateFlow(0L)
    val transmittedId: StateFlow<Long>
        get() = _transmittedId
    fun setTransmittedId(value:Long){
        _transmittedId.value = value
        if (value != 0L){
            val currentTask = allTasks.value?.filter { it.id == value }?.firstOrNull()
            _nameTextFieldEdit.value = currentTask.let { if (it != null) it.name else  "error"}
            _descriptionTextFieldEdit.value = currentTask.let { if (it != null) it.description else  "error"}
            _currentDate.value = currentTask.let { if (it != null) it.date else  "error"}
            _transmittedParentId.value = currentTask.let { if (it != null) it.parent_id else  0L}
        }
    }


    private val _transmittedParentId = MutableStateFlow(0L)
    val transmittedParentId: StateFlow<Long>
        get() = _transmittedParentId
    fun setTransmittedParentId(value:Long){
        _transmittedParentId.value = value
    }

    private val _currentBoardId = MutableStateFlow(0L)
    val currentBoardId: StateFlow<Long>
        get() = _currentBoardId
    fun setCurrentBoardId(value:Long){
        _currentBoardId.value = value
        setParentBoardId(allBoards.value?.filter { it.id == _currentBoardId.value }!!.lastOrNull()
            .let { if (it != null) it.parent_id else 1 })
    }

    private val _nameTextFieldEdit = MutableStateFlow("")
    val nameTextFieldEdit: StateFlow<String>
        get() = _nameTextFieldEdit
    fun setNameTextFieldEdit(value:String){
        _nameTextFieldEdit.value = value
    }

    private val _descriptionTextFieldEdit = MutableStateFlow("")
    val descriptionTextFieldEdit: StateFlow<String>
        get() = _descriptionTextFieldEdit
    fun setDescriptionTextFieldEdit(value:String){
        _descriptionTextFieldEdit.value = value
    }

    private val _currentDate = MutableStateFlow(SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()))
    val currentDate: StateFlow<String>
        get() = _currentDate
    fun setCurrentDate(value:String){
        _currentDate.value = value
    }



    @OptIn(ExperimentalMaterialApi::class)
    private val _stateModalEdit = MutableStateFlow(ModalBottomSheetState(ModalBottomSheetValue.Hidden))
    @OptIn(ExperimentalMaterialApi::class)
    val stateModalEdit: StateFlow<ModalBottomSheetState>
        get() = _stateModalEdit

    @OptIn(ExperimentalMaterialApi::class)
    private val _stateModalMain = MutableStateFlow(ModalBottomSheetState(ModalBottomSheetValue.Hidden))
    @OptIn(ExperimentalMaterialApi::class)
    val stateModalMain: StateFlow<ModalBottomSheetState>
        get() = _stateModalMain



    @OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
    private val _relocationRequester = MutableStateFlow(BringIntoViewRequester())
    @OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
    val relocationRequester: StateFlow<BringIntoViewRequester>
        get() = _relocationRequester


    private val _indexAnimateTarget = MutableStateFlow(0)
    val indexAnimateTarget: StateFlow<Int>
        get() {
            refreshIndexAnimateTarget()
            return _indexAnimateTarget
        }
    fun setIndexAnimateTarget(value:Int){
        _indexAnimateTarget.value = value
    }
    fun refreshIndexAnimateTarget(){
        setIndexAnimateTarget(allBoards.value?.filter { a ->
            (a.parent_id == _parentBoardId.value && !allTasks.value?.filter { it.parent_id == a.id }
                .isNullOrEmpty())
        }!!.indexOf(
            if (allBoards.value!!.filter { it.id == _currentBoardId.value }.lastOrNull() != null)
                allBoards.value!!.filter { it.id == _currentBoardId.value }.lastOrNull()
            else
                BoardModel(0, "", "", 0)
        ).let { if (it == -1) 0 else it })
        println("EEEEEEE refresh=${_indexAnimateTarget.value}")
    }

    private val _parentBoardId = MutableStateFlow(1L)
    val parentBoardId: StateFlow<Long>
        get() = _parentBoardId
    fun setParentBoardId(value:Long){
        _parentBoardId.value = value
    }


    @OptIn(ExperimentalMaterialApi::class)
    private var _animateNavController:MutableStateFlow<NavHostController?> = MutableStateFlow(null)
    @OptIn(ExperimentalMaterialApi::class)
    val animateNavController: StateFlow<NavHostController?>
        get() = _animateNavController
    fun setAnimateNavController(value:NavHostController){
        _animateNavController.value = value
    }





    @OptIn(ExperimentalMaterialApi::class)
    private var _screenStateMain:MutableStateFlow<Screen> = MutableStateFlow(Screen.Home)
    @OptIn(ExperimentalMaterialApi::class)
    val screenStateMain: StateFlow<Screen>
        get() = _screenStateMain
    fun setScreenStateMain(value:Screen){
        _screenStateMain.value = value
    }



    @OptIn(ExperimentalMaterialApi::class)
    private var _selectedItem:MutableStateFlow<Int> = MutableStateFlow(0)
    @OptIn(ExperimentalMaterialApi::class)
    val selectedItem: StateFlow<Int>
        get() = _selectedItem
    fun setSelectedItem(value:Int){
        _selectedItem.value = value
    }


    @OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
    private var _pagerState = MutableStateFlow(PagerState())
    @OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
    val pagerState: StateFlow<PagerState>
        get() = _pagerState
    @OptIn(ExperimentalPagerApi::class)
    fun setPagerState(value:PagerState){
        _pagerState.value = value
    }






    @OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
    private var _scaleContent = MutableStateFlow(0.4f)
    @OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
    val scaleContent: StateFlow<Float>
        get() = _scaleContent
    @OptIn(ExperimentalPagerApi::class)
    fun setScaleContent(value:Float){
        _scaleContent.value = value
    }



    @OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
    private var _offsetContent = MutableStateFlow(Offset(0f,0f))
    @OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
    val offsetContent: StateFlow<Offset>
        get() = _offsetContent
    @OptIn(ExperimentalPagerApi::class)
    fun setOffsetContent(value:Offset){
        _offsetContent.value = value
    }






    @OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
    private var _screenHierarchyState: MutableStateFlow<Screen> = MutableStateFlow(Screen.Hierarchy)
    @OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
    val screenHierarchyState: StateFlow<Screen>
        get() = _screenHierarchyState
    @OptIn(ExperimentalPagerApi::class)
    fun setScreenHierarchyState(value:Screen){
        _screenHierarchyState.value = value
    }




    @OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
    private var _transformableState: MutableStateFlow<TransformableState> = MutableStateFlow(
        TransformableState(onTransformation = { zoomChange, offsetChange, rotationChange ->
            if (_scaleContent.value*zoomChange >= 0.1){
                setScaleContent(_scaleContent.value*zoomChange)
                setOffsetContent(_offsetContent.value+offsetChange)
            }
        }))
    @OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
    val transformableState: StateFlow<TransformableState>
        get() = _transformableState
    @OptIn(ExperimentalPagerApi::class)
    fun setTransformableState(value:TransformableState){
        _transformableState.value = value
    }







    private val _openDialogEditing = MutableStateFlow(false)
    val openDialogEditing: StateFlow<Boolean>
        get() = _openDialogEditing
    fun setOpenDialogEditing(value:Boolean){
        _openDialogEditing.value = value
    }

    private val _openDialogAdding = MutableStateFlow(false)
    val openDialogAdding: StateFlow<Boolean>
        get() = _openDialogAdding
    fun setOpenDialogAdding(value:Boolean){
        _openDialogAdding.value = value
    }





    private val _nameBoardAlertDialogAdding = MutableStateFlow("")
    val nameBoardAlertDialogAdding: StateFlow<String>
        get() = _nameBoardAlertDialogAdding
    fun setNameBoardAlertDialogAdding(value:String){
        _nameBoardAlertDialogAdding.value = value
    }


    private val _nameBoardAlertDialogEditing = MutableStateFlow("")
    val nameBoardAlertDialogEditing: StateFlow<String>
        get() = _nameBoardAlertDialogEditing
    fun setNameBoardAlertDialogEditing(value:String){
        _nameBoardAlertDialogEditing.value = value
    }


}