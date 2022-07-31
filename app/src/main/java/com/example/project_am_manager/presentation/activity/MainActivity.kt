package com.example.project_am_manager.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project_am_manager.data.repositoryImpl.BoardListRepositoryImpl
import com.example.project_am_manager.data.repositoryImpl.TaskListRepositoryImpl
import com.example.project_am_manager.presentation.application.MainApplication
import com.example.project_am_manager.presentation.application.appComponent
import com.example.project_am_manager.presentation.ui.compose.MainApp
import com.example.project_am_manager.presentation.viewmodel.MainViewModel
import com.example.project_am_manager.presentation.viewmodel.MainViewModelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    val viewModel: MainViewModel by viewModels(factoryProducer = {mainViewModelFactory})


    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)

        setContent {
            MainApp(viewModel)
        }
    }
}
