package com.example.project_am_manager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.BoardModel
import kotlinx.coroutines.launch
import viewmodel.MainViewModel

@OptIn(ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun EditScreen(
    viewModel: MainViewModel
) {
    val stateModalEdit by viewModel.stateModalEdit.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()
    val descriptionTextFieldEdit by viewModel.descriptionTextFieldEdit.collectAsState()
    val nameTextFieldEdit by viewModel.nameTextFieldEdit.collectAsState()
    val transmittedParentId by viewModel.transmittedParentId .collectAsState()
    val relocationRequester by viewModel.relocationRequester .collectAsState()

    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = nameTextFieldEdit,
            onValueChange ={viewModel.setNameTextFieldEdit(it)},
            textStyle = TextStyle(Color(70, 169, 240), fontSize = 22.sp, fontWeight = FontWeight.W400),
            placeholder = {
                Text(text = "Название", fontSize = 22.sp, fontWeight = FontWeight.W400, color = Color.LightGray)
            },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, unfocusedIndicatorColor = Color.Unspecified, cursorColor = Color(70, 169, 240)),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 32.dp)
        )
        Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier
            .padding(horizontal =  12.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
            , horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.clickable {
                if (stateModalEdit.isVisible){
                    scope.launch { stateModalEdit.hide() }
                }else{
                    scope.launch { stateModalEdit.show() }
                }
            }) {
                Text(
                    fontWeight = FontWeight.W200, fontSize = 14.sp, color = Color.Black,
                    text = allBoards.filter { it.id == transmittedParentId }.firstOrNull()
                    .let { if(it != null) it.name else "Error" })
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_down_24), contentDescription = "", modifier = Modifier
                    .padding(PaddingValues(start = 8.dp, top = 5.dp))
                    .size(14.dp)
                    .alpha(0.7f)
                    , tint = Color.Black)
            }
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
        TextField(
            value = descriptionTextFieldEdit,
            onValueChange ={viewModel.setDescriptionTextFieldEdit(it)},
            textStyle = TextStyle(Color.DarkGray, fontSize = 16.sp, fontWeight = FontWeight.W300),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White,
                unfocusedIndicatorColor = Color.Unspecified, cursorColor = Color.DarkGray),
            placeholder = {
                Text(text = "Содержание", fontSize = 16.sp, fontWeight = FontWeight.W300, color = Color.LightGray)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .bringIntoViewRequester(relocationRequester)
        )
    }
}