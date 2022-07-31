package com.example.project_am_manager.presentation.ui.compose.components.floatingactionbutton

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.R
import com.example.project_am_manager.presentation.activity.TaskActivity
import com.example.project_am_manager.presentation.viewmodel.MainViewModel
import routing.Screen

@Composable
fun MainExtendedFloatingActionButton(
    viewModel: MainViewModel
) {
    val screenStateMain by viewModel.screenStateMain.collectAsState()
    val parentBoardId by viewModel.parentBoardId.collectAsState()

    val context = LocalContext.current
    if (screenStateMain == Screen.Home) {

        FloatingActionButton(
            backgroundColor = Color.Black,
            contentColor = Color.White,
            content = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_plus_24),
                    contentDescription = ""
                )
            },
            onClick = {
                val intent = TaskActivity.newIntentAddTask(context, parentBoardId)
                context.startActivity(intent)
            },
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        )
    }
}