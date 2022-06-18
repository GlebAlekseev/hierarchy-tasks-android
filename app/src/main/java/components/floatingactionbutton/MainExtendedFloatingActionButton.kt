package components.floatingactionbutton

import android.content.Intent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.DataMain
import com.example.project_am_manager.R
import com.example.project_am_manager.TaskActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import routing.Screen

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun MainExtendedFloatingActionButton(
    dataMain: DataMain
){
    val context = LocalContext.current
    if (dataMain.screenState.value == Screen.Home){
        FloatingActionButton(
            backgroundColor = Color.Black,
            contentColor = Color.White,
            content = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_plus_24), contentDescription = "") },
            onClick = {
                context.startActivity(Intent(context, TaskActivity::class.java))
            },
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        )

    }


}