package com.example.project_am_manager

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable

import viewmodel.MainViewModel
import viewmodel.MainViewModelFactory

class TaskActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels(factoryProducer = {
        MainViewModelFactory(
            this,
            (application as TManagerApplication).dependencyInjector.repositoryTask,
            (application as TManagerApplication).dependencyInjector.repositoryBoard
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id:Long = intent.getLongExtra(MainActivity.TRANSMITTED_ID,0).toLong()

        setContent {
            TaskApp(viewModel,id)
        }
    }
}