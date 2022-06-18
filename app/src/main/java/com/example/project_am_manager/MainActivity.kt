package com.example.project_am_manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import viewmodel.MainViewModel
import viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
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
            TManagerApp(viewModel)
        }
    }

    companion object{
        const val TRANSMITTED_ID = "transmitted_id"
    }
}

