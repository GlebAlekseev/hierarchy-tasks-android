@file:OptIn(ExperimentalPagerApi::class)

package com.example.project_am_manager.presentation.ui.compose.screen.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.presentation.ui.compose.components.TaskCard
import com.example.project_am_manager.presentation.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@Composable
fun MainHomeScreen(
    viewModel: MainViewModel,
) {
    val scope = rememberCoroutineScope()
    val pagerState by viewModel.pagerState.collectAsState()
    val parentBoardId by viewModel.parentBoardId.collectAsState()
    val boardsForParent by viewModel.getBoardsForParent(parentBoardId).observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            },
        ) {
            boardsForParent.forEachIndexed { index, board ->
                Tab(
                    text = {
                        Text(
                            text = board.name,
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                            color = Color(53, 156, 252)
                        )
                    },
                    selected = if (pagerState.currentPage == index) {
                        scope.launch {
                            viewModel.setCurrentBoardId(boardsForParent[index].id)
                        }
                        true
                    } else false,
                    onClick = {
                        scope.launch {
                            viewModel.setCurrentBoardId(boardsForParent[index].id)
                        }
                    }, selectedContentColor = Color.Black, unselectedContentColor = Color.LightGray
                )
            }
        }
        HorizontalPager(
            count = boardsForParent.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val tasksForBoardsForParent
                    by viewModel.getTasksForBoardsForParent(boardsForParent, page)
                        .observeAsState(emptyList())
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                itemsIndexed(items = tasksForBoardsForParent,
                    itemContent = { index, item ->
                        TaskCard(viewModel, item)
                        val count = tasksForBoardsForParent.size
                        if (index == count - 1) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                            )
                        }
                    })
            }
        }
    }
}