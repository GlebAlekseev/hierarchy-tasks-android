package com.example.project_am_manager.presentation.ui.compose.bottombar

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.project_am_manager.presentation.ui.compose.struct.NavigationItem
import com.example.project_am_manager.presentation.viewmodel.MainViewModel


@Composable
fun MainBottomBar(
    viewModel: MainViewModel
) {
    val animateNavController by viewModel.animateNavController.collectAsState()
    val selectedNavPage by viewModel.selectedNavPage.collectAsState()
    BottomNavigation {
        NavigationItem.items.forEach {
            BottomNavigationItem(
                selected = selectedNavPage == it.index,
                onClick = {
                    viewModel.setSelectedNavPage(it.index)
                    animateNavController?.navigate(it.screen)
                },
                icon = {
                    Icon(imageVector = ImageVector.vectorResource(
                        id = it.vectorResourceId
                    ),
                        contentDescription = stringResource(id = it.contentDescriptionResourceId),
                        tint = Color.Black,
                        modifier = Modifier
                            .drawBehind {
                                if (selectedNavPage == it.index) {
                                    drawRoundRect(
                                        Color(135, 138, 245),
                                        Offset(-size.width, -size.height * 0.4f / 2),
                                        Size(size.width * 3f, size.height * 1.4f),
                                        cornerRadius = CornerRadius(50f, 50f),
                                        alpha = 0.1f
                                    )
                                }
                            }
                    )
                }
            )
        }
    }
}