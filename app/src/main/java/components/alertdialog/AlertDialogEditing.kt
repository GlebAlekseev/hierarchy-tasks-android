package components.alertdialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
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
    openDialog: MutableState<Boolean>,
    nameBoardState: MutableState<TextFieldValue>,
    viewModel: MainViewModel,
    board: BoardModel?,
    currentBoard: MutableState<Long>,
    stateModal: ModalBottomSheetState,
    scope: CoroutineScope
) {
    val scopeOther = rememberCoroutineScope()
    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Редактирование доски")
            },
            text = {
                Column() {
                    Text("Название")
                    TextField(value = nameBoardState.value, onValueChange = {nameBoardState.value = it})
                    Text(text = allBoards.filter { it.id == currentBoard.value }.firstOrNull()?.name.orEmpty(), modifier = Modifier.clickable{
                        // Вызов выбора currentBoard
                        if (stateModal.isVisible){
                            scopeOther.launch {
                                openDialog.value = true
                                stateModal.hide()

                            }
                        }else{
                            scopeOther.launch {
                                openDialog.value = false
                                stateModal.show()

                            }
                        }


                    })
                }


            },
            confirmButton = {
                Button(

                    onClick = {
                        if(board.let { if (it != null) it.id else 1L } != 1L ){
                            viewModel.updateBoard(
                                BoardModel(board.let { if (it != null) it.id else 0L },nameBoardState.value.text, SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(
                                Date()
                            ),allBoards.filter { it.id == currentBoard.value }.firstOrNull().let { if (it != null) it.id else 0L })
                            )
                        }
                        openDialog.value = false

                    }) {
                    Text("Сохранить")
                }
            }
        )
    }

}