package components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import domain.model.TaskModel
import com.example.project_am_manager.EditInputs
import com.example.project_am_manager.TaskActivity


@Composable
fun AlertDialogSave(openDialog: MutableState<Boolean>, editInputs: EditInputs, id:Long) {
    val context = LocalContext.current as TaskActivity
    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false

                        if (id == 0L) {
                            val model = TaskModel(
                                0,
                                editInputs.nameState.value.text,
                                editInputs.descriptionState.value.text,
                                editInputs.currentDate,
                                Color.DarkGray,
                                editInputs.parentBoardState.value
                            )
                            println("MODEL_INSERT=$model")
                            editInputs.viewModel.insertTask(model)
                        } else {
                            val model = TaskModel(
                                id,
                                editInputs.nameState.value.text,
                                editInputs.descriptionState.value.text,
                                editInputs.currentDate,
                                Color.DarkGray,
                                editInputs.parentBoardState.value
                            )
                            println("MODEL_UPDATE=$model")
                            editInputs.viewModel.updateTask(model)
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