package com.example.project_am_manager.presentation.ui.compose.screen.main


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.RequestDisallowInterceptTouchEvent
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.R
import com.example.project_am_manager.domain.entity.BoardItem
import com.example.project_am_manager.presentation.viewmodel.MainViewModel
import routing.MainScreen
import kotlin.math.roundToInt


@Composable
fun MainHierarchyScreen(
    viewModel: MainViewModel
) {
    TransformableArea(viewModel)
}

@Composable
fun TransformableArea(
    viewModel: MainViewModel
) {
    val state_scroll_horizontal = rememberScrollState()
    val state_scroll_vertical = rememberScrollState()
    val scaleContent by viewModel.scaleContent.collectAsState()
    val offsetContent by viewModel.offsetContent.collectAsState()
    val screenHierarchyState by viewModel.screenHierarchyState.collectAsState()
    val transformableState by viewModel.transformableState.collectAsState()

    Box(
        Modifier
            .fillMaxSize()
            .horizontalScroll(state_scroll_horizontal)
            .verticalScroll(state_scroll_vertical)
            .background(Color.White)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consumeAllChanges()
                    viewModel.setOffsetContent(offsetContent + dragAmount)
                }
            }
            .transformable(state = transformableState)
            .offset { IntOffset((offsetContent.x).roundToInt(), (offsetContent.y).roundToInt()) }
            .graphicsLayer(
                scaleX = scaleContent,
                scaleY = scaleContent,
            ), contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .background(Color.White), horizontalAlignment = Alignment.Start
        ) {
            when (screenHierarchyState) {
                MainScreen.Hierarchy2 -> BuildingHierarchy(viewModel)
                MainScreen.Hierarchy -> BuildingHierarchy(viewModel)
                else -> {}
            }
        }

    }
}

@Composable
fun BuildingHierarchy(
    viewModel: MainViewModel
) {
    Row() {
        val listOffsetsBoards: MutableList<boardBlock> = remember {
            mutableListOf()
        }
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            Board(
                viewModel,
                viewModel.getBoard(1),
                listOffsetsBoards,
            )
        }
        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            ColumnZ(
                viewModel,
                viewModel.getBoard(1).id,
                listOffsetsBoards
            )
        }
    }


}

@Composable
fun ColumnZ(
    viewModel: MainViewModel,
    idBoard: Long,
    listOffsetsBoards: MutableList<boardBlock>
) {
    if (idBoard == 0L) return
    val startY = remember {
        mutableStateOf(0f)
    }
    val boardsForParentWithoutRoot by viewModel.getBoardsForParentWithoutRoot(idBoard)
        .observeAsState(emptyList())
    boardsForParentWithoutRoot.filter { it.parent_id == idBoard && it.id != it.parent_id }
        .forEachIndexed { index, it ->
            Row(modifier = Modifier
                .background(Color.White)
                .onGloballyPositioned {
                    if (index == boardsForParentWithoutRoot.size - 1) startY.value =
                        it.positionInParent().y
                }
                .drawBehind {
                    if (boardsForParentWithoutRoot.size == 1) {
                    } else if (index == 0) {
                        drawLine(
                            start = Offset(x = 0f, y = 0f + size.height / 2),
                            end = Offset(x = 0f, y = size.height),
                            color = Color(53, 146, 252),
                            strokeWidth = 8F
                        )
                    } else if (index == boardsForParentWithoutRoot.size - 1) {
                        drawLine(
                            start = Offset(x = 0f, y = 0f),
                            end = Offset(x = 0f, y = size.height - size.height / 2),
                            color = Color(53, 146, 252),
                            strokeWidth = 8F
                        )
                    } else {
                        drawLine(
                            start = Offset(x = 0f, y = 0f),
                            end = Offset(x = 0f, y = size.height),
                            color = Color(53, 146, 252),
                            strokeWidth = 8F
                        )
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Board(viewModel, it, listOffsetsBoards)
                }
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    ColumnZ(viewModel, it.id, listOffsetsBoards)
                }
            }
        }
}

data class boardBlock(
    var board: BoardItem,
    var offset: MutableState<Offset>,
    var selected: MutableState<Boolean>,
    var size: MutableState<IntSize>
)

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun Board(
    viewModel: MainViewModel,
    board: BoardItem,
    listOffsetsBoards: MutableList<boardBlock>
) {
    val screenHierarchyState by viewModel.screenHierarchyState.collectAsState()
    val allBoards: List<BoardItem> by viewModel.getBoardList().observeAsState(emptyList())
    var offset = remember { mutableStateOf(Offset(0f, 0f)) }
    val selected = remember { mutableStateOf(false) }
    val sizeState = remember { mutableStateOf(IntSize.Zero) }
    val scale = animateFloatAsState(if (selected.value) 1.2f else 1f)
    val offset_global = remember {
        mutableStateOf(Offset.Zero)
    }
    val requestDisallowInterceptTouchEvent = RequestDisallowInterceptTouchEvent()
    requestDisallowInterceptTouchEvent.invoke(true)

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .offset { IntOffset(offset.value.x.toInt(), offset.value.y.toInt()) }
        .scale(scale.value)
        .padding(10.dp)
        .border(BorderStroke(2.dp, Color(0, 0, 0)), shape = RoundedCornerShape(40.dp))
        .padding(10.dp)
        .drawBehind {
            sizeState.value = IntSize(size.width.toInt(), size.height.toInt())
            drawLine(
                start = Offset(x = size.width + 20, y = size.height / 2),
                end = Offset(x = size.width + 52, y = size.height / 2),
                color = Color(53, 146, 252),
                strokeWidth = 8F
            )
            drawLine(
                start = Offset(x = -20f, y = size.height / 2),
                end = Offset(x = -52f, y = size.height / 2),
                color = Color(53, 146, 252),
                strokeWidth = 8F
            )
        }
        .onGloballyPositioned {
            offset_global.value = it.positionInRoot()
            if (!listOffsetsBoards.contains(
                    boardBlock(
                        board,
                        offset_global,
                        selected,
                        sizeState
                    )
                )
            )
                listOffsetsBoards.add(boardBlock(board, offset_global, selected, sizeState))
        }
        .pointerInput(Unit) {
            detectDragGestures(
                onDrag = { pic, offset_ ->
                    offset.value += offset_
                    listOffsetsBoards.forEach {
                        if (it.offset != offset_global && it.board != null) {
                            if (it.offset.value.x + it.size.value.width / 2 >= offset_global.value.x && it.offset.value.x - it.size.value.width / 2 <= offset_global.value.x &&
                                it.offset.value.y + it.size.value.height / 2 >= offset_global.value.y && it.offset.value.y - it.size.value.height / 2 <= offset_global.value.y &&
                                (listOffsetsBoards.filter { a -> a.selected.value == true }.size == 0
                                        || listOffsetsBoards.filter { a ->
                                    a.selected.value == true && it.board == a.board
                                            && it.board.let { if (it != null) it.id else 0L } == a.board.let { if (it != null) it.id else -1L }
                                }.size == 1
                                        )
                                && !getAllParentsBoardIds(
                                    it.board,
                                    allBoards
                                ).contains(board.let { if (it != null) it.id else 0L })
                            ) {
                                it.selected.value = true
                            } else {
                                it.selected.value = false
                            }
                        }
                    }
                },
                onDragEnd = {
                    if (listOffsetsBoards.filter { it.selected.value == true }.size >= 1) {
                        if (screenHierarchyState == MainScreen.Hierarchy) {
                            viewModel.setScreenHierarchyState(MainScreen.Hierarchy2)
                        } else {
                            viewModel.setScreenHierarchyState(MainScreen.Hierarchy)
                        }
                    } else {
                        // Сбросить назад
                        if (screenHierarchyState == MainScreen.Hierarchy) {
                            viewModel.setScreenHierarchyState(MainScreen.Hierarchy2)
                        } else {
                            viewModel.setScreenHierarchyState(MainScreen.Hierarchy)
                        }
                    }
                })
        }
        .combinedClickable(
            onClick = {
                viewModel.setCurrentBoardId(board.id)
            },
            onLongClick = {
                viewModel.setCurrentBoardId(board.id)
                expanded = true
            },
        )
        .padding(15.dp)
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_bookmark_24),
            contentDescription = "",
            tint = Color(53, 156, 252),
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = board.name.orEmpty(),
            color = Color.Black
        )
        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                viewModel.setOpenDialogAdding(true)
            }) {
                Text("Добавить", style = TextStyle(Color(53, 156, 252)))
            }
            DropdownMenuItem(onClick = {
                expanded = false
                viewModel.setOpenDialogEditing(true)
            }) {
                Text(text = "Редактировать", style = TextStyle(Color(53, 156, 252)))
            }
            if (board.let { if (it != null) it.id else 1L } != 1L) {
                DropdownMenuItem(onClick = {
                    if (board.id != 1L) {
                        viewModel.deleteBoard(viewModel.getBoard(board.id))
                    }
                    expanded = false
                }) {
                    Text(text = "Удалить", style = TextStyle(Color(53, 156, 252)))
                }
            }
        }
    }
}


fun getAllParentsBoardIds(board: BoardItem, listBoard: List<BoardItem>): List<Long> {
    var parentsBoard = mutableListOf<Long>()
    var parentBoard: BoardItem = board
    parentsBoard.add(parentBoard.id)
    var i = 0
    while (i < listBoard.size) {
        parentBoard = listBoard.filter { parentBoard.parent_id == it.id }.last()
        parentsBoard.add(parentBoard.id)
        if (parentBoard.id == parentBoard.parent_id) break
        i++
    }
    return parentsBoard
}








