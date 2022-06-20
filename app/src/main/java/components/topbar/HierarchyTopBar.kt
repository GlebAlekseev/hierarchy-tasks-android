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
import com.example.project_am_manager.R
import com.google.accompanist.pager.ExperimentalPagerApi
import domain.model.BoardModel
import viewmodel.MainViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HierarchyTopBar(
  viewModel: MainViewModel
) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = {
            viewModel.setScaleContent(0.4f)
            viewModel.setOffsetContent(Offset(0f,0f))
        }) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_hierarchy_24), contentDescription ="" )
        }
    }
}
