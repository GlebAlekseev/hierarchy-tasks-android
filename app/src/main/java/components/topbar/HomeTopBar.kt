package components.topbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_am_manager.DataMain
import com.example.project_am_manager.R
import com.google.accompanist.pager.ExperimentalPagerApi
import domain.model.BoardModel
import kotlinx.coroutines.launch
import viewmodel.MainViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class, ExperimentalUnitApi::class)
@Composable
fun HomeTopBar(
    dataMain: DataMain
) {
    val scope = rememberCoroutineScope()
    val allBoards: List<BoardModel> by dataMain.viewModel.allBoards.observeAsState(emptyList())

    Row(modifier = Modifier
        .fillMaxWidth()

        .padding(horizontal = 20.dp)
        .padding(bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(modifier = Modifier.clickable {
            if (dataMain.state.isVisible){
                scope.launch { dataMain.state.hide() }
            }else{
                scope.launch { dataMain.state.show() }
            }
        }){
            Text(text = allBoards.filter { it.id==dataMain.currentBoard.value }.firstOrNull().let { if (it != null) it.name else "" }
                ,color=Color.Black, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_down_24), contentDescription = "", modifier = Modifier.padding(
                PaddingValues(start = 15.dp, top = 5.dp)
            ), tint = Color.Black)
        }
    }
}