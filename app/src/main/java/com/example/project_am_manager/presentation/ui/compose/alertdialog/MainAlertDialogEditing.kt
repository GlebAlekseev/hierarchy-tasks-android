package com.example.project_am_manager.presentation.ui.compose.alertdialog

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_am_manager.domain.entity.BoardItem
import com.example.project_am_manager.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun MainAlertDialogEditing(
    viewModel: MainViewModel,
) {
    val openDialogEditing by viewModel.openDialogEditing.collectAsState()
    if (openDialogEditing) {
        val nameBoardAlertDialogEditing by viewModel.nameBoardAlertDialogEditing.collectAsState()
        val currentHierarchyBoardId by viewModel.currentHierarchyBoardId.collectAsState()
        val parentBoardId by viewModel.parentBoardId.collectAsState()
        val scope = rememberCoroutineScope()
        viewModel.setNameBoardAlertDialogEditing(viewModel.getBoard(currentHierarchyBoardId).name)

        AlertDialog(
            onDismissRequest = {
                viewModel.setOpenDialogEditing(false)
            },
            title = {
                Text(
                    text = "Редактирование доски",
                    style = TextStyle(
                        color = Color(14, 112, 204),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                )
            },
            text = {
                Column() {
                    Text(
                        "Название:",
                        style = TextStyle(color = Color.Black),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    TextField(
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        value = nameBoardAlertDialogEditing,
                        onValueChange = { viewModel.setNameBoardAlertDialogEditing(it) },
                        textStyle = TextStyle(Color.Gray),
                        modifier = Modifier
                            .offset(0.dp, 10.dp)
                            .border(1.5f.dp, Color.Black, RoundedCornerShape(25.dp))
                            .padding(5.dp, 6.dp)
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(PaddingValues(top = 4.dp))
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Родитель: " + viewModel.getBoard(parentBoardId).name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterVertically)
                                .offset(0.dp, 10.dp),
                            textDecoration = TextDecoration.Underline,
                            style = TextStyle(textAlign = TextAlign.Center)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = Color(56, 152, 242),
                        contentColor = Color.White
                    ),
                    onClick = {
                        scope.launch {
                            if (currentHierarchyBoardId != 1L) {
                                viewModel.editBoard(
                                    BoardItem(
                                        nameBoardAlertDialogEditing,
                                        SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()),
                                        parentBoardId,
                                        currentHierarchyBoardId,
                                    )
                                )
                            }
                            viewModel.setOpenDialogEditing(false)
                            viewModel.setNameBoardAlertDialogEditing("")
                        }
                    }) {
                    Text("Сохранить")
                }
            }
        )
    }
}