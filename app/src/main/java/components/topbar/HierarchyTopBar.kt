package components.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.DataMain
import com.example.project_am_manager.R
import com.google.accompanist.pager.ExperimentalPagerApi
import domain.model.BoardModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HierarchyTopBar(
    dataMain: DataMain
) {
    val allBoards: List<BoardModel> by dataMain.viewModel.allBoards.observeAsState(emptyList())

//    val SHD = LocalConfiguration.current.screenHeightDp-165f
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = {
            dataMain.scale_content.value = 0.4f
            dataMain.offset_content.value = Offset(0f,0f)
        }) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_hierarchy_24), contentDescription ="" )
        }
    }
}
