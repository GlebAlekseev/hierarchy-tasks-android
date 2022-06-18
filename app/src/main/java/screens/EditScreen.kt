package com.example.project_am_manager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.model.BoardModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
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
    Column() {
        TextField(
            value = editInputs.nameState.value,
            onValueChange ={editInputs.nameState.value = it},
            modifier = Modifier
                .background(color = Color.Blue)
                .fillMaxWidth()
        )


        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(Color.Red), horizontalArrangement = Arrangement.SpaceBetween) {
            Row() {
                Text(text = editInputs.currentDate)
            }
            Row(modifier = Modifier.clickable {
                if (editInputs.stateModal.isVisible){
                    scope.launch { editInputs.stateModal.hide() }
                }else{
                    scope.launch { editInputs.stateModal.show() }
                }
            }) {
                Text(text = allBoards.filter { it.id == editInputs.parentBoardState.value }.firstOrNull().let { if(it != null) it.name else "None" })
            }

        }


        TextField(
            value = editInputs.descriptionState.value,
            onValueChange ={editInputs.descriptionState.value = it},
            modifier = Modifier
                .background(color = Color.Cyan)
                .fillMaxSize()
        )
    }

}