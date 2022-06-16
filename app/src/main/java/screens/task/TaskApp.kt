package screens.task

import android.util.Log
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import com.example.project_am_manager.R

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.ui.theme.Project_AM_ManagerTheme
import com.skyyo.expandablelist.cards.BoardsScreen
import components.AlertDialogSave
import domain.model.TaskModel
import kotlinx.coroutines.*
import routing.BackButtonAction
import routing.ScreenTask
import routing.TaskRouter
import viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaskApp(viewModel: MainViewModel,id:Long) {
    Project_AM_ManagerTheme{
        AppContent(viewModel,id)

    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppContent(viewModel: MainViewModel,id:Long){
    val stateModal = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val nameState = remember { mutableStateOf(TextFieldValue()) }
    var currentDate: String = SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date())
    val descriptionState = remember { mutableStateOf(TextFieldValue()) }
    val parentBoardState = remember { mutableStateOf(0L) }

    if (id != 0L){
        val allTasks: List<TaskModel> by viewModel.allTasks.observeAsState(emptyList())
        nameState.value = TextFieldValue(allTasks.filter { it.id == id }.firstOrNull().let { if (it != null) it.name else  "error"})
        descriptionState.value = TextFieldValue(allTasks.filter { it.id == id }.firstOrNull().let { if (it != null) it.description else  "error"})
        currentDate = allTasks.filter { it.id == id }.firstOrNull().let { if (it != null) it.date else  "error"}
        parentBoardState.value =  allTasks.filter { it.id == id }.firstOrNull().let { if (it != null) it.parent_id else  0}
    }

    val editInputs = EditInputs(viewModel,stateModal,scope,scaffoldState,nameState,currentDate,descriptionState,parentBoardState)



    // Анимация?!
    Crossfade(targetState = TaskRouter.currentScreen) { screenState: MutableState<ScreenTask> ->

        Scaffold(
            topBar = {TopTaskAppBar(screenState,descriptionState)},
            content = { MainScreenTaskContainer(screenState,editInputs) }
        )

    }


    ModalBottomSheetChoose(editInputs)


    // Если id == 0 - добавление
    // Иначе - редактирование

    // Передать данные полей в диалог, где выполнить операцию

    val openDialog = remember { mutableStateOf(false)  }
    AlertDialogSave(openDialog,editInputs,id)
    val allTasks: List<TaskModel> by editInputs.viewModel.allTasks.observeAsState(emptyList())
    val context = LocalContext.current as TaskActivity

    BackButtonAction {

        val name_descr_parent_db = allTasks.filter { it.id == id }.lastOrNull().let { if (it != null) it.name + it.description + it.parent_id else "" }

        if (id == 0L && editInputs.nameState.value.text.length == 0 && editInputs.descriptionState.value.text.length == 0 ){
            openDialog.value = false
            context.finish()
        }else if (name_descr_parent_db == editInputs.nameState.value.text + editInputs.descriptionState.value.text + editInputs.parentBoardState.value.toString()){
            openDialog.value = false
            context.finish()
        }else{
            openDialog.value = true
        }

    }







}

@Composable
fun TopTaskAppBar(
    screenState: MutableState<ScreenTask>,
    descriptionState: MutableState<TextFieldValue>
) {

    val localBackPressed = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Row() {
            IconButton(
                onClick = {
                    localBackPressed?.onBackPressed()
                          },
                content = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_back_arrow_24), contentDescription = "back")},
            )
            Text(text = "Символов: " + descriptionState.value.text.length)
        }
        Row() {
            if (screenState.value == ScreenTask.Edit){
            IconButton(
                onClick = {screenState.value = ScreenTask.View},
                content = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_view_24), contentDescription = "view")},
            )
            }else if (screenState.value == ScreenTask.View){
                IconButton(
                    onClick = {screenState.value = ScreenTask.Edit},
                    content = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_edit_24), contentDescription = "edit")},
                )
            }
        }
    }




}





@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreenTaskContainer(
    screenState: MutableState<ScreenTask>,
    editInputs: EditInputs
//    screenState: MutableState<ScreenTask>,
//    viewModel: MainViewModel,
//    stateModal: ModalBottomSheetState,
//    descriptionState: MutableState<TextFieldValue>,
//    nameState: MutableState<TextFieldValue>,
//    currentDate: String,
//    parentBoardState: Long
){


    // Запросить данные по id из Intent
    // Если интенет пуст, тогда ничего


    when (screenState.value) {
        ScreenTask.Edit -> EditScreen(editInputs)
//        ScreenTask.Edit -> EditScreen(viewModel,stateModal,descriptionState,nameState,currentDate,parentBoardState)
        ScreenTask.View -> ViewScreen(editInputs)
//        ScreenTask.View -> ViewScreen(viewModel,descriptionState,nameState,currentDate,parentBoardState)
    }




}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheetChoose(
    editInputs: EditInputs
//    state: ModalBottomSheetState,
//    scope: CoroutineScope,
//    viewModel: MainViewModel
) {

    ModalBottomSheetLayout(
        sheetState = editInputs.stateModal,
        sheetContent = {

            BoardsScreen(editInputs = editInputs, isAll = true)
        }
    ) {


    }


}


@OptIn(ExperimentalMaterialApi::class)
data class EditInputs(
    val viewModel: MainViewModel,
    val stateModal: ModalBottomSheetState,
    val scope: CoroutineScope,
    val scaffoldState: ScaffoldState,
    val nameState: MutableState<TextFieldValue>,
    var currentDate: String,
    val descriptionState: MutableState<TextFieldValue>,
    var parentBoardState: MutableState<Long>
    )