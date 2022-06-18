package components.topbar

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.R
import routing.ScreenTask


@Composable
fun TopTaskAppBar(
    screenState: MutableState<ScreenTask>,
    descriptionState: MutableState<TextFieldValue>
) {

    val localBackPressed = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Row() {
            IconButton(
                onClick = {
                    localBackPressed?.onBackPressed()
                },
                content = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_back_arrow_24), contentDescription = "back") },
            )
            Text(text = "Символов: " + descriptionState.value.text.length)
        }
        Row() {
            if (screenState.value == ScreenTask.Edit){
                IconButton(
                    onClick = {screenState.value = ScreenTask.View},
                    content = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_view_24), contentDescription = "view") },
                )
            }else if (screenState.value == ScreenTask.View){
                IconButton(
                    onClick = {screenState.value = ScreenTask.Edit},
                    content = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_edit_24), contentDescription = "edit") },
                )
            }
        }
    }




}