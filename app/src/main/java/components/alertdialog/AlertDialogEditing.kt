package components.alertdialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import domain.model.BoardModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlertDialogEditing(
    viewModel: MainViewModel,
    board: BoardModel?
) {
    val openDialogEditing by viewModel.openDialogEditing.collectAsState()
    val nameBoardAlertDialogEditing by viewModel.nameBoardAlertDialogEditing.collectAsState()
    val currentBoardId by viewModel.currentBoardId.collectAsState()
    val scopeOther = rememberCoroutineScope()
    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())

    if (openDialogEditing) {
        AlertDialog(
            onDismissRequest = {
                viewModel.setOpenDialogEditing(false)
            },
            title = {
                Text(text = "Редактирование доски")
            },
            text = {
                Column() {
                    Text("Название")
                    TextField(value = nameBoardAlertDialogEditing, onValueChange = {viewModel.setNameBoardAlertDialogEditing(it)})
                    println("$$$$$$$$$$$$$$$$$${board?.name}")
                    Text(text = allBoards.filter { it.id == currentBoardId }.firstOrNull()?.name.orEmpty(),
                        )
                }


            },
            confirmButton = {
                Button(

                    onClick = {
//                        if(board.let { if (it != null) it.id else 1L } != 1L ){
                            viewModel.updateBoard(
                                BoardModel(currentBoardId,nameBoardAlertDialogEditing, SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(
                                Date()
                            ),allBoards.filter { it.id == currentBoardId }.firstOrNull().let { if (it != null) it.parent_id else 1L })
                            )
//                        }
                        viewModel.setOpenDialogEditing(false)

                    }) {
                    Text("Сохранить")
                }
            }
        )
    }

}