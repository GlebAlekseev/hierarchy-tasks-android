package components.floatingactionbutton


import android.content.Intent
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
//import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.MainActivity
import com.example.project_am_manager.R
import com.example.project_am_manager.TaskActivity
import com.google.accompanist.pager.ExperimentalPagerApi


import routing.Screen
import viewmodel.MainViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun MainExtendedFloatingActionButton(
    viewModel: MainViewModel
){
    val screenStateMain by viewModel.screenStateMain.collectAsState()
    val parentBoardId by viewModel.parentBoardId .collectAsState()

    val context = LocalContext.current
    if (screenStateMain == Screen.Home){

        FloatingActionButton(
            backgroundColor = Color.Black,
            contentColor = Color.White,
            content = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_plus_24), contentDescription = "") },
            onClick = {
                val intent = Intent(context, TaskActivity::class.java)
                intent.putExtra(MainActivity.CURRENT_BOARD, parentBoardId)
                context.startActivity(intent)
            },
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        )
    }
}