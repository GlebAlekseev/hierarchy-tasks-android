package com.example.project_am_manager.presentation.ui.compose.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.R
import com.example.project_am_manager.presentation.viewmodel.MainViewModel


@Composable
fun MainHierarchyTopBar(
    viewModel: MainViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(134, 124, 247),
                        Color(62, 125, 250), //purple-blue grad
                    )
                )
            )
            .height(60.dp)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            modifier = Modifier.offset(0.dp, 5.dp),
            onClick = {
                viewModel.setScaleContent(0.4f)
                viewModel.setOffsetContent(Offset(0f, 0f))
            }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_hierarchy_new_24),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
            )
        }
    }
}
