package com.example.project_am_manager

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.project_am_manager.ui.theme.Project_AM_ManagerTheme
import data.database.dbmapper.DbMapperBoardImpl
import data.database.model.BoardDbModel
import data.database.model.TaskDbModel
import domain.model.BoardModel
import domain.model.TaskModel
import screens.task.TaskApp
import viewmodel.MainViewModel
import viewmodel.MainViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalView
import com.skyyo.expandablelist.cards.CardsViewModel

class MainActivity : ComponentActivity() {

    companion object{
        const val TRANSMITTED_ID = "transmitted_id"
    }


    private val cardsViewModel by viewModels<CardsViewModel>()

    private val viewModel: MainViewModel by viewModels(factoryProducer = {
        MainViewModelFactory(
            this,
            (application as TManagerApplication).dependencyInjector.repositoryTask,
            (application as TManagerApplication).dependencyInjector.repositoryBoard
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        setContent {


            TManagerApp(viewModel,cardsViewModel)
        }

//        viewModel.insertBoard(BoardModel(1,"root",
//            SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()),1))


    }

}

