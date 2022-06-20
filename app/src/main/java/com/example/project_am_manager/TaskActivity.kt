package com.example.project_am_manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import viewmodel.MainViewModel
import viewmodel.MainViewModelFactory

class TaskActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels(factoryProducer = {
        MainViewModelFactory(
            this,
            (application as TManagerApplication).dependencyInjector.repositoryBoard,
            (application as TManagerApplication).dependencyInjector.repositoryTask,

        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id:Long = intent.getLongExtra(MainActivity.TRANSMITTED_ID,0).toLong()
        val id_current_board:Long = intent.getLongExtra(MainActivity.CURRENT_BOARD,0).toLong()


        viewModel.setTransmittedId(id)
        if (viewModel.transmittedParentId.value == 0L)
            viewModel.setTransmittedParentId(id_current_board)

        setContent {
            TaskApp(viewModel)
        }
    }
}