package components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import domain.model.BoardModel
import domain.model.TaskModel
import screens.task.EditInputs
import screens.task.TaskActivity


@Composable
fun AlertDialogSave(openDialog: MutableState<Boolean>,editInputs:EditInputs,id:Long) {
    val context = LocalContext.current as TaskActivity
//    val allTasks: List<TaskModel> by editInputs.viewModel.allTasks.observeAsState(emptyList())

            if (openDialog.value) {

                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onCloseRequest.
                        openDialog.value = false
                    },
//                    title = {
//                        Text(text = if (id == 0L) "Добавление" else "Редактирование")
//                    },
//                    text = {
//                        Text("Here is a text ")
//                    },
                    confirmButton = {
                        Button(

                            onClick = {
                                openDialog.value = false

                                if (id == 0L) {
                                    editInputs.viewModel.insertTask(TaskModel(
                                        0,
                                        editInputs.nameState.value.text,
                                        editInputs.descriptionState.value.text,
                                        editInputs.currentDate,
                                        Color.DarkGray,
                                        editInputs.parentBoardState.value
                                    ))
                                }else{
                                    editInputs.viewModel.updateTask(TaskModel(
                                        id,
                                        editInputs.nameState.value.text,
                                        editInputs.descriptionState.value.text,
                                        editInputs.currentDate,
                                        Color.DarkGray,
                                        editInputs.parentBoardState.value
                                    ))
                                }
                                context.finish()
                            }) {
                            Text(if (id == 0L) "Добавить" else "Сохранить")
                        }
                    },
                    dismissButton = {
                        Button(

                            onClick = {
                                openDialog.value = false
                                context.finish()

                            }) {
                            Text("Игнорировать")
                        }
                    }
                )
            }



}