package components.alertdialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                Text(text = "Добавление доски", style = TextStyle(color = Color(14, 112, 204),fontSize = 25.sp, fontWeight = FontWeight.Bold, fontFamily= FontFamily.SansSerif))
            },
            text = {
                Column() {
                    Text("Название:", style = TextStyle(color = Color.Black), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    TextField(colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, disabledIndicatorColor = Color.Transparent),value = nameBoardAlertDialogAdding,
                        onValueChange = {viewModel.setNameBoardAlertDialogAdding(it)}, textStyle = TextStyle(Color.Gray),
                        modifier = androidx.compose.ui.Modifier
                            .offset(0.dp,10.dp)
                            .border(1.5f.dp, Color.Black, RoundedCornerShape(25.dp))
                            .padding(5.dp,6.dp))
                }

            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = Color(56, 152, 242),
                        contentColor = Color.White),

                    onClick = {
                        val boardTmp = BoardModel(
                            0,
                            nameBoardAlertDialogAdding,
                            SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()),
                            currentBoardId)
                        viewModel.insertBoard(boardTmp)
                        viewModel.setOpenDialogAdding(false)
                        viewModel.setNameBoardAlertDialogAdding("")

                    }) {
                    Text("Добавить")
                }
            }
        )
    }

}