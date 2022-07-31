package com.example.project_am_manager.presentation.ui.compose.routing

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import routing.TaskScreen

interface Router <T>{
    var currentScreen: MutableState<T>
    var previousScreen: MutableState<T>
    fun navigateTo(destination: T)
    fun goBack()
}