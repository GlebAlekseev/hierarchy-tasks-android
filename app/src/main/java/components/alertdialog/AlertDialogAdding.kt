package components.alertdialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import domain.model.BoardModel
import viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AlertDialogAdding(openDialog: MutableState<Boolean>, nameBoardState: MutableState<TextFieldValue>, viewModel: MainViewModel, board: BoardModel?) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Добавление доски")
            },
            text = {
                Column() {
                    Text("Название")
                    TextField(value = nameBoardState.value, onValueChange = {nameBoardState.value = it})
                }

            },
            confirmButton = {
                Button(

                    onClick = {
                        viewModel.insertBoard(
                            BoardModel(0,nameBoardState.value.text, SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(
                            Date()
                        ),board.let { if (it != null) it.id else 0L })
                        )
                        openDialog.value = false

                    }) {
                    Text("Добавить")
                }
            }
        )
    }

}