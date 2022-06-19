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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.DataMain
import com.example.project_am_manager.MainActivity
import com.example.project_am_manager.R
import com.example.project_am_manager.TaskActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.skyyo.expandablelist.cards.EXPAND_ANIMATION_DURATION
//import com.skyyo.expandablelist.cards.EXPAND_ANIMATION_DURATION
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
                val intent = Intent(context, TaskActivity::class.java)
                intent.putExtra(MainActivity.CURRENT_BOARD, dataMain.currentBoard.value)
                context.startActivity(intent)
            },
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        )

    }


}