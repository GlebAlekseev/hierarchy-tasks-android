package com.example.project_am_manager.presentation.ui.compose.screen.main//package com.example.project_am_manager.presentation.ui.compose.screens

import androidx.compose.runtime.Composable
import com.example.project_am_manager.presentation.ui.compose.components.LazyColumnWithCalendar
import com.example.project_am_manager.presentation.viewmodel.MainViewModel

@Composable
fun MainHistoryScreen(
    viewModel: MainViewModel
) {
    LazyColumnWithCalendar(viewModel)
}
