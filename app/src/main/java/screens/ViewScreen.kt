package com.example.project_am_manager

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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

@Composable
fun ViewScreen(
    editInputs: EditInputs
//    viewModel: MainViewModel,
//    descriptionState: MutableState<TextFieldValue>,
//    nameState: MutableState<TextFieldValue>,
//    currentDate: String,
//    parentBoardState:Long
) {
//    BackButtonAction {
//        TaskRouter.goBack()
//    }
    val allBoards: List<BoardModel> by editInputs.viewModel.allBoards.observeAsState(emptyList())
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = editInputs.nameState.value.text.toString(), fontSize = 22.sp, fontWeight = FontWeight.W400, color = Color.Black, modifier = Modifier
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
                text = allBoards.filter { it.id == editInputs.parentBoardState.value }.firstOrNull()
                    .let { if(it != null) it.name else  allBoards.filter { it.id == editInputs.idCurrentBoard }.firstOrNull().let { if (it != null) it.name else "Error"} })

            Row() {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_time_24), contentDescription = "",tint=Color.Black, modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .alpha(0.6f)
                    .size(18.dp)
                    .padding(top = 4.dp)
                )
                Text(text =  editInputs.currentDate, textAlign = TextAlign.Right, fontWeight = FontWeight.W200, fontSize = 14.sp, color = Color.Black)
            }

        }

        Text(text = editInputs.descriptionState.value.text.toString(), fontSize = 16.sp, fontWeight = FontWeight.W300, color = Color.DarkGray,
        modifier = Modifier
            .padding(24.dp))
    }


}