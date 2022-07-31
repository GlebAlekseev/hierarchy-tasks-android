package com.example.project_am_manager.presentation.ui.compose.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.domain.entity.TaskItem
import com.example.project_am_manager.presentation.viewmodel.MainViewModel
import com.himanshoe.kalendar.ui.Kalendar
import com.himanshoe.kalendar.ui.KalendarType
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun LazyColumnWithCalendar(viewModel: MainViewModel) {
    val currentDate: MutableState<String> = remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd").format(Date()))
    }
    val tasksOnDate by viewModel.getTasksOnDate(currentDate.value).observeAsState(emptyList())

    val zeroTaskModel = listOf(TaskItem("", "", "", Color.Black, 0, 0))
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(zeroTaskModel + tasksOnDate, itemContent = { index, item ->
            if (index == 0) {
                Kalendar(
                    viewModel = viewModel,
                    kalendarType = KalendarType.Firey(),
                    onCurrentDayClick = { day, _ ->
                        currentDate.value = day.toString()
                    },
                    errorMessage = {
                    })
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
            } else {
                TaskCard(viewModel, item)
                if (index == tasksOnDate.size) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    )
                }
            }
        })
    }
}