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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun EditScreen(
    editInputs: EditInputs
//    viewModel: MainViewModel,
//    stateModal:ModalBottomSheetState,
//    descriptionState: MutableState<TextFieldValue>,
//    nameState: MutableState<TextFieldValue>,
//    currentDate: String,
//    parentBoardState:Long
) {

    val allBoards: List<BoardModel> by editInputs.viewModel.allBoards.observeAsState(emptyList())
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = editInputs.nameState.value,
            onValueChange ={editInputs.nameState.value = it},
            textStyle = TextStyle(Color.Black, fontSize = 22.sp, fontWeight = FontWeight.W400),
            placeholder = {
                Text(text = "Название", fontSize = 22.sp, fontWeight = FontWeight.W400, color = Color.LightGray)
            },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, unfocusedIndicatorColor = Color.Unspecified, cursorColor = Color.Black),
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
                if (editInputs.stateModal.isVisible){
                    scope.launch { editInputs.stateModal.hide() }
                }else{
                    scope.launch { editInputs.stateModal.show() }
                }
            }) {
                Text(
                    fontWeight = FontWeight.W200, fontSize = 14.sp, color = Color.Black,
                    text = allBoards.filter { it.id == editInputs.parentBoardState.value }.firstOrNull()
                    .let { if(it != null) it.name else  allBoards.filter { it.id == editInputs.idCurrentBoard }.firstOrNull().let { if (it != null) it.name else "Error"} })

                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_down_24), contentDescription = "", modifier = Modifier
                    .padding(PaddingValues(start = 8.dp, top = 3.dp))
                    .size(12.dp)
                    .alpha(0.7f)
                    , tint = Color.Black)
            }


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
        val relocationRequester = remember { BringIntoViewRequester() }
        TextField(
            value = editInputs.descriptionState.value,
            onValueChange ={editInputs.descriptionState.value = it},
            textStyle = TextStyle(Color.DarkGray, fontSize = 16.sp, fontWeight = FontWeight.W300),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, unfocusedIndicatorColor = Color.Unspecified, cursorColor = Color.DarkGray),
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