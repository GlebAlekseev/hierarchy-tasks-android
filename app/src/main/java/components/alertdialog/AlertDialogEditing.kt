package components.alertdialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val parentBoardId by viewModel.parentBoardId.collectAsState()
    val scopeOther = rememberCoroutineScope()
    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())

    viewModel.setNameBoardAlertDialogEditing(allBoards.filter { it.id == currentBoardId }.firstOrNull()?.name.orEmpty())
    if (openDialogEditing) {
        AlertDialog(
            onDismissRequest = {
                viewModel.setOpenDialogEditing(false)
            },
            title = {
                Text(text = "Редактирование доски",style = TextStyle(color = Color(14, 112, 204),fontSize = 25.sp, fontWeight = FontWeight.Bold, fontFamily= FontFamily.SansSerif))
            },
            text = {
                Column() {
                    Text("Название:", style = TextStyle(color = Color.Black), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    TextField(colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, disabledIndicatorColor = Color.Transparent), value = nameBoardAlertDialogEditing,
                        onValueChange = {viewModel.setNameBoardAlertDialogEditing(it)},textStyle = TextStyle(Color.Gray),
                        modifier = androidx.compose.ui.Modifier
                            .offset(0.dp, 10.dp)
                            .border(1.5f.dp, Color.Black, RoundedCornerShape(25.dp))
                            .padding(5.dp,6.dp))
                    println("$$$$$$$$$$$$$$$$$${board?.name}")
                    Row(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(PaddingValues(top = 4.dp))
                        .fillMaxWidth()
                        ) {
                         Text(text = "Родитель: "+ allBoards.filter { it.id == parentBoardId }.firstOrNull()?.name.orEmpty(),
                             fontSize = 15.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier
                                 .fillMaxWidth().align(Alignment.CenterVertically).offset(0.dp,10.dp), textDecoration = TextDecoration.Underline, style = TextStyle(textAlign = TextAlign.Center)
                                 )

                    }

                }


            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = Color(56, 152, 242),
                        contentColor = Color.White),

                    onClick = {
//                        if(board.let { if (it != null) it.id else 1L } != 1L ){
                            viewModel.updateBoard(
                                BoardModel(currentBoardId,nameBoardAlertDialogEditing, SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(
                                Date()
                            ),allBoards.filter { it.id == currentBoardId }.firstOrNull().let { if (it != null) it.parent_id else 1L })
                            )
//                        }
                        viewModel.setOpenDialogEditing(false)
                        viewModel.setNameBoardAlertDialogEditing("")
                    }) {
                    Text("Сохранить")
                }
            }
        )
    }

}