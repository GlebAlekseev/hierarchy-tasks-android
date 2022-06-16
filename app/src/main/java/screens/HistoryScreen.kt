package screens

import android.content.Intent
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_am_manager.MainActivity
import com.example.project_am_manager.R
import com.himanshoe.kalendar.ui.Kalendar
import com.himanshoe.kalendar.ui.KalendarType
import domain.model.TaskModel
import routing.BackButtonAction
import routing.TManagerRouter
import screens.task.TaskActivity
import viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(viewModel: MainViewModel) {
    BackButtonAction {
        TManagerRouter.goBack()
    }


    LazyColumnDemo(viewModel)



}

@Composable
fun LazyColumnDemo(viewModel: MainViewModel) {
    val allTasks: List<TaskModel> by viewModel.allTasks.observeAsState(emptyList())
    val formatter = SimpleDateFormat("dd:MM:yyyy hh:mm:ss", Locale.US)
    val currentDate: MutableState<String> = remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd").format(Date()))
    }

    val zeroTaskModel = listOf<TaskModel>(TaskModel(0,"","","",Color.Black,0))
    val context = LocalContext.current



//    allTasksToday
//    allTasks.filter { it.date ==  1}
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(zeroTaskModel +allTasks.filter {
            currentDate.value == SimpleDateFormat("yyyy-MM-dd").format(formatter.parse(it.date)) }, itemContent = { index, item ->
            if (index == 0){
                // CAlendar
                Kalendar(viewModel=viewModel,kalendarType = KalendarType.Firey(), onCurrentDayClick = { day, event ->
                    //handle the date click listener
//                        println("day=$day and simpledata=${SimpleDateFormat("yyyy-MM-dd").format(Date())}")
                    // На основе даты делаю выборку по всем таскам

                    currentDate.value = day.toString()
//                    allTasksToday.value = allTasks.filter { SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(formatter.parse(it.date)) == day.toString() }



                }, errorMessage = {
                    //Handle the error if any

                })
            }else{
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.Black)
                    .padding(10.dp)
                    .clickable
                    {
                        val intent = Intent(context, TaskActivity::class.java)
                        intent.putExtra(MainActivity.TRANSMITTED_ID, item.id)
                        context.startActivity(intent)
                    }
                ) {
                    Text(text = "item name=${item.name} date=${item.date}", style = TextStyle(fontSize = 25.sp))
                }
            }




        })
    }
}