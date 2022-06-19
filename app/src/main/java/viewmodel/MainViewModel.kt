package viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyo.expandablelist.cards.ExpandableCardModel
import data.repository.RepositoryBoard
import data.repository.RepositoryTask
import domain.model.BoardModel
import domain.model.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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


    ////////////////////////////////////
    // View Saveable















}