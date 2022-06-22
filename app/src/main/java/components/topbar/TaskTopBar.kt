package components.topbar

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.R
import routing.ScreenTask
import viewmodel.MainViewModel


@Composable
fun TopTaskAppBar(
    screenState: MutableState<ScreenTask>,
    viewModel: MainViewModel
) {
    val descriptionTextFieldEdit by viewModel.descriptionTextFieldEdit.collectAsState()

    val localBackPressed = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(
            Brush.linearGradient(
                colors = listOf(
                    Color(62, 125, 250), //purple-blue grad
                    Color(134, 124, 247)
                )
            )
        )
        .padding(horizontal = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Row() {
            IconButton(
                modifier = Modifier.offset(0.dp,5.dp),
                onClick = {
                    localBackPressed?.onBackPressed()
                },
                content = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_back_arrow_24),
                    contentDescription = "back", tint = Color.White) },
            )
        }
        Row() {
            if (screenState.value == ScreenTask.Edit){
                IconButton(
                    modifier = Modifier.offset(0.dp,5.dp),
                    onClick = {screenState.value = ScreenTask.View},
                    content = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_view_new_24),
                        contentDescription = "view", tint = Color.White) },
                )
            }else if (screenState.value == ScreenTask.View){
                IconButton(
                    modifier = Modifier.offset(0.dp,5.dp),
                    onClick = {screenState.value = ScreenTask.Edit},
                    content = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_edit_new_24),
                        contentDescription = "edit", tint = Color.White) },
                )
            }
        }
    }
}