package components.alertdialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.TextFieldValue
import domain.model.BoardModel
import viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AlertDialogAdding(viewModel: MainViewModel) {
    val openDialogAdding by viewModel.openDialogAdding.collectAsState()
    val nameBoardAlertDialogAdding by viewModel.nameBoardAlertDialogAdding.collectAsState()
    val currentBoardId by viewModel.currentBoardId.collectAsState()
    if (openDialogAdding) {
        AlertDialog(
            onDismissRequest = {
                viewModel.setOpenDialogAdding(false)
            },
            title = {
                Text(text = "Добавление доски")
            },
            text = {
                Column() {
                    Text("Название")
                    TextField(value = nameBoardAlertDialogAdding, onValueChange = {viewModel.setNameBoardAlertDialogAdding(it)})
                }

            },
            confirmButton = {
                Button(

                    onClick = {
                        val boardTmp = BoardModel(
                            0,
                            nameBoardAlertDialogAdding,
                            SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()),
                            currentBoardId)
                        viewModel.insertBoard(boardTmp)
                        viewModel.setOpenDialogAdding(false)

                    }) {
                    Text("Добавить")
                }
            }
        )
    }

}