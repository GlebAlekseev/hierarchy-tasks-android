package components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_am_manager.R
import com.google.accompanist.pager.ExperimentalPagerApi
import domain.model.BoardModel
import kotlinx.coroutines.launch
import viewmodel.MainViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class, ExperimentalUnitApi::class)
@Composable
fun HomeTopBar(
  viewModel: MainViewModel
) {
    val scope = rememberCoroutineScope()
    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
    val stateModalMain by viewModel.stateModalMain.collectAsState()
    val parentBoardId by viewModel.parentBoardId.collectAsState()

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
        .padding(horizontal = 23.dp, vertical = 5.dp)
        .padding(bottom = 15.dp), horizontalArrangement = Arrangement.SpaceBetween)
        {
        Row(modifier = Modifier.clickable {
            if (stateModalMain.isVisible){
                scope.launch { stateModalMain.hide() }
            }else{
                scope.launch { stateModalMain.show() }
            }
        }){
            Text(text = allBoards.filter { it.id==parentBoardId }.firstOrNull().let { if (it != null) it.name else "" }, modifier = Modifier.offset(0.dp, 7.dp)
                ,color=Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_down_24), contentDescription = "", modifier = Modifier.padding(
                PaddingValues(start = 10.dp, top = 10.dp)
            ).offset(0.dp,8.dp), tint = Color.White)
        }
    }
}