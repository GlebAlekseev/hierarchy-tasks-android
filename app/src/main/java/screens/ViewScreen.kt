package com.example.project_am_manager

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.BoardModel
import viewmodel.MainViewModel

@Composable
fun ViewScreen(
    viewModel: MainViewModel
) {
    val currentDate by viewModel.currentDate.collectAsState()
    val descriptionTextFieldEdit by viewModel.descriptionTextFieldEdit.collectAsState()
    val nameTextFieldEdit by viewModel.nameTextFieldEdit.collectAsState()
    val transmittedParentId by viewModel.transmittedParentId .collectAsState()

    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = nameTextFieldEdit, fontSize = 22.sp, fontWeight = FontWeight.W400, color = Color(70, 169, 240), modifier = Modifier
            .padding(16.dp)
            .padding(horizontal = 32.dp)
        )
        Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier
            .padding(horizontal =  12.dp))
        Row() {
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
            , horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                fontWeight = FontWeight.W200, fontSize = 14.sp, color = Color.Black,
                text = allBoards.filter { it.id == transmittedParentId }.firstOrNull()
                    .let { if(it != null) it.name else "Error" })
            Row() {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_time_24),
                    contentDescription = "",tint=Color.Black, modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .alpha(0.6f)
                    .size(18.dp)
                    .padding(top = 4.dp)
                )
                Text(text = currentDate, textAlign = TextAlign.Right, fontWeight = FontWeight.W200, fontSize = 14.sp, color = Color.Black)
            }

        }
        Row() {
            Text(
                text = "Символов: " + descriptionTextFieldEdit.length, color = Color.Black,
                modifier = Modifier
                    .padding(12.dp)
            )
        }
        Text(text = descriptionTextFieldEdit, fontSize = 16.sp, fontWeight = FontWeight.W300, color = Color.DarkGray,
        modifier = Modifier
            .padding(24.dp))
    }
}