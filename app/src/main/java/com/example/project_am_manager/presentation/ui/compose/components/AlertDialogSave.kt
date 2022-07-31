package com.example.project_am_manager.presentation.ui.compose.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.domain.entity.TaskItem
import com.example.project_am_manager.presentation.activity.TaskActivity
import com.example.project_am_manager.presentation.viewmodel.TaskViewModel


@Composable
fun AlertDialogSave(viewModel: TaskViewModel) {
    val openDialogSave by viewModel.openDialogSave.collectAsState()

    val transmittedId by viewModel.transmittedId.collectAsState()
    val descriptionTextFieldEdit by viewModel.descriptionTextFieldEdit.collectAsState()
    val nameTextFieldEdit by viewModel.nameTextFieldEdit.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()
    val transmittedParentId by viewModel.transmittedParentId.collectAsState()

    val context = LocalContext.current as TaskActivity
    if (openDialogSave) {
        AlertDialog(
            onDismissRequest = {
                viewModel.setOpenDialogSave(false)
            },
            confirmButton = {
                Button(modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .border(1.5f.dp, Color.Black, shape = RoundedCornerShape(2.dp))
                    .width(130.dp),
                    onClick = {
                        viewModel.setOpenDialogSave(false)
                        if (transmittedId == 0L) {
                            val model = TaskItem(
                                nameTextFieldEdit,
                                descriptionTextFieldEdit,
                                currentDate,
                                Color.DarkGray,
                                transmittedParentId,
                                0,
                            )
                            viewModel.addTask(model)
                        } else {
                            val model = TaskItem(
                                nameTextFieldEdit,
                                descriptionTextFieldEdit,
                                currentDate,
                                Color.DarkGray,
                                transmittedParentId,
                                transmittedId,
                            )
                            viewModel.editTask(model)
                        }
                        context.finish()
                    }) {
                    Text(
                        if (transmittedId == 0L) "Добавить" else "Сохранить",
                        style = TextStyle(Color(70, 169, 240))
                    )
                }
            },
            dismissButton = {
                Button(modifier = Modifier
                    .offset(9.0f.dp, 0.dp)
                    .border(1.5f.dp, Color.Black, shape = RoundedCornerShape(2.dp))
                    .width(130.dp),
                    onClick = {
                        viewModel.setOpenDialogSave(false)
                        context.finish()
                    }) {
                    Text("Отменить", style = TextStyle(Color(70, 169, 240)))
                }
            }
        )
    }
}