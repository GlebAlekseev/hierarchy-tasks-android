package com.example.project_am_manager.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.project_am_manager.TaskApp
import com.example.project_am_manager.presentation.application.appComponent
import com.example.project_am_manager.presentation.viewmodel.TaskViewModel
import com.example.project_am_manager.presentation.viewmodel.TaskViewModelFactory
import javax.inject.Inject


class TaskActivity : ComponentActivity() {

    @Inject
    lateinit var taskViewModelFactory: TaskViewModelFactory
    val viewModel: TaskViewModel by viewModels(factoryProducer = { taskViewModelFactory })

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)
        val id: Long = intent.getLongExtra(TRANSMITTED_ID, 0)
        val id_current_board: Long = intent.getLongExtra(CURRENT_BOARD, 0)

        if (savedInstanceState == null) {
            viewModel.setTransmittedId(id)
            if (viewModel.transmittedParentId.value == 0L)
                viewModel.setTransmittedParentId(id_current_board)
        }

        setContent {
            TaskApp(viewModel)
        }
    }

    companion object {
        const val TRANSMITTED_ID = "transmitted_id"
        const val CURRENT_BOARD = "current_board"

        fun newIntentAddTask(context: Context, currentBoard: Long): Intent {
            val intent = Intent(context, TaskActivity::class.java)
            intent.putExtra(CURRENT_BOARD, currentBoard)
            return intent
        }

        fun newIntentEditTask(context: Context, transmittedId: Long, currentBoardId: Long): Intent {
            val intent = Intent(context, TaskActivity::class.java)
            intent.putExtra(TRANSMITTED_ID, transmittedId)
            intent.putExtra(CURRENT_BOARD, currentBoardId)
            return intent
        }
    }
}