package screens.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import routing.BackButtonAction
import routing.TaskRouter
import viewmodel.MainViewModel

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
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = editInputs.nameState.value.text.toString())
        Text(text = editInputs.currentDate)
        Text(text = editInputs.descriptionState.value.text.toString())
    }


}