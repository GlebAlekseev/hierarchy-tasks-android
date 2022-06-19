package screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_am_manager.DataMain
import com.example.project_am_manager.MainActivity
import com.himanshoe.kalendar.ui.Kalendar
import com.himanshoe.kalendar.ui.KalendarType
import domain.model.TaskModel
import routing.BackButtonAction
import routing.TManagerRouter
import com.example.project_am_manager.TaskActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HistoryScreen(
    dataMain: DataMain
) {
//    BackButtonAction {
//        TManagerRouter.goBack()
//    }
    LazyColumnDemo(dataMain)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LazyColumnDemo(dataMain: DataMain) {
    val allTasks: List<TaskModel> by dataMain.viewModel.allTasks.observeAsState(emptyList())
    val formatter = SimpleDateFormat("dd:MM:yyyy hh:mm:ss", Locale.US)
    val currentDate: MutableState<String> = remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd").format(Date()))
    }

    val zeroTaskModel = listOf<TaskModel>(TaskModel(0,"","","",Color.Black,0))
    val context = LocalContext.current

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(zeroTaskModel +allTasks.filter {
            currentDate.value == SimpleDateFormat("yyyy-MM-dd").format(formatter.parse(it.date)) }, itemContent = { index, item ->
            if (index == 0){
                Kalendar(viewModel=dataMain.viewModel,kalendarType = KalendarType.Firey(), onCurrentDayClick = { day, event ->
                    currentDate.value = day.toString()
                }, errorMessage = {

                })
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp))
            }
            else{
                ToastContent(dataMain,item)
                if (index == allTasks.filter {currentDate.value == SimpleDateFormat("yyyy-MM-dd").format(formatter.parse(it.date)) }.size){
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp))
                }
            }
        })
    }
}