package screens


import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.project_am_manager.MainActivity
import com.example.project_am_manager.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.skyyo.expandablelist.cards.BoardsScreen
import components.alertdialog.AlertDialogAdding
import components.alertdialog.AlertDialogEditing
import domain.model.BoardModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import routing.BackButtonAction
import routing.Screen
import routing.TManagerRouter
import viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun HierarchyScreen(
    viewModel: MainViewModel
) {

    TransformableSample(viewModel)
}
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class, ExperimentalPagerApi::class)
@Composable
fun TransformableSample(
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
                    viewModel.setOffsetContent(offsetContent+dragAmount)
                }
            }
            .transformable(state = transformableState)
            .offset { IntOffset((offsetContent.x).roundToInt(), (offsetContent.y).roundToInt()) }
            .graphicsLayer(
                scaleX = scaleContent,
                scaleY = scaleContent,
            ), contentAlignment = Alignment.CenterStart
    ){

            Column(modifier = Modifier
                .background(Color.White), horizontalAlignment = Alignment.Start) {
                when(screenHierarchyState){
                    Screen.Hierarchy2 -> BuildingHierarchy(viewModel)
                    Screen.Hierarchy -> BuildingHierarchy(viewModel)
                    else -> {}
                }
            }

    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun BuildingHierarchy(
    viewModel: MainViewModel
){
    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
    val currentBoardId by viewModel.currentBoardId.collectAsState()

        Row() {
            val listOffsetsBoards: MutableList<boardBlock> = remember {
                mutableListOf()
            }

            Column(modifier = Modifier
                .align(Alignment.CenterVertically)
            ) {
                // Второй root
                Board(viewModel, allBoards.filter { it.id == it.parent_id }.firstOrNull(),listOffsetsBoards)
            }
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                // Второй root
                ColumnZ(viewModel, allBoards.filter { it.id == it.parent_id }.firstOrNull().let { if (it != null) it.id else 0L },listOffsetsBoards)
            }
        }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ColumnZ(
    viewModel: MainViewModel,
    idBoard: Long,
    listOffsetsBoards: MutableList<boardBlock>
) {
    if (idBoard == 0L) return
    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())

    val endY = remember {
        mutableStateOf(0f)
    }
    val startY = remember {
        mutableStateOf(0f)
    }
    allBoards.filter { it.parent_id == idBoard && it.id != it.parent_id }
        .forEachIndexed { index, it ->
            Row(modifier = Modifier
                .background(Color.White)
                .onGloballyPositioned {
                    if (index == allBoards.filter { it.parent_id == idBoard && it.id != it.parent_id }.size - 1) startY.value =
                        it.positionInParent().y
                }
                .drawBehind {
                    if (allBoards.filter { it.parent_id == idBoard && it.id != it.parent_id }.size == 1) {
                    } else if (index == 0) {
                        drawLine(
                            start = Offset(x = 0f, y = 0f + size.height / 2),
                            end = Offset(x = 0f, y = size.height),
//                        end = Offset(x = 0f, y = startY.value + size.height / 2 + 2),
                            color = Color.Gray,
                            strokeWidth = 8F
                        )
                    } else if (index == allBoards.filter { it.parent_id == idBoard && it.id != it.parent_id }.size - 1) {
                        drawLine(
                            start = Offset(x = 0f, y = 0f),
                            end = Offset(x = 0f, y = size.height - size.height / 2),
//                        end = Offset(x = 0f, y = startY.value + size.height / 2 + 2),
                            color = Color.Gray,
                            strokeWidth = 8F
                        )

                    } else {
                        drawLine(
                            start = Offset(x = 0f, y = 0f),
                            end = Offset(x = 0f, y = size.height),
                            color = Color.Gray,
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

data class boardBlock(var board:BoardModel?,var offset: MutableState<Offset>,var selected: MutableState<Boolean>,var size: MutableState<IntSize>)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun Board(
    viewModel: MainViewModel,
    board: BoardModel?,
    listOffsetsBoards: MutableList<boardBlock>
) {
    val screenHierarchyState by viewModel.screenHierarchyState.collectAsState()

    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
    var offset = remember { mutableStateOf(Offset(0f, 0f)) }
    val selected = remember { mutableStateOf(false) }
    val sizeState = remember { mutableStateOf(IntSize.Zero) }
    val scale = animateFloatAsState(if (selected.value) 1.2f else 1f)
    val offset_global = remember {
        mutableStateOf(Offset.Zero)
    }
    val nameBoardStateAdding = remember { mutableStateOf(TextFieldValue()) }
    val nameBoardStateEditing = remember { mutableStateOf(TextFieldValue()) }
    val scope = rememberCoroutineScope()

    AlertDialogAdding(viewModel)
    AlertDialogEditing(viewModel, board)

    val requestDisallowInterceptTouchEvent = RequestDisallowInterceptTouchEvent()
    requestDisallowInterceptTouchEvent.invoke(true)

    var motionEvent by remember { mutableStateOf(Recomposer.State.Idle) }
    val pressuredState = remember {
        mutableStateOf(1f)
    }
    val offsetDrag = remember {
        mutableStateOf(0f)
    }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .offset { IntOffset(offset.value.x.toInt(), offset.value.y.toInt()) }
        .scale(scale.value)
        .padding(10.dp)
        .border(BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(5.dp))
        .padding(10.dp)
        .drawBehind {
            sizeState.value = IntSize(size.width.toInt(), size.height.toInt())
            drawLine(
                start = Offset(x = size.width + 20, y = size.height / 2),
                end = Offset(x = size.width + 52, y = size.height / 2),
                color = Color.Gray,
                strokeWidth = 8F
            )
            drawLine(
                start = Offset(x = -20f, y = size.height / 2),
                end = Offset(x = -52f, y = size.height / 2),
                color = Color.Gray,
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
//                                    || listOffsetsBoards.filter { a-> a.selected.value == true && a.board.id ==  } == 1 11111111111111&& it.board != a.board
                                        || listOffsetsBoards.filter { a ->
                                    a.selected.value == true && it.board == a.board
                                            && it.board.let { if (it != null) it.id else 0L } == a.board.let { if (it != null) it.id else -1L }
                                }.size == 1
                                        )
//                            && board?.id != board?.parent_id
                                && !getAllParentsBoardIds(
                                    it.board,
                                    allBoards
                                ).contains(board.let { if (it != null) it.id else 0L })
                            // Если it имеет parent - board, то нет
                            ) {
//                            intersectId.value = it.board.let { if (it != null) it.id else 0L }
//                            println("***intersectId=${intersectId.value}")
                                it.selected.value = true
//                            return@forEach
                            } else {
//                            println("**${listOffsetsBoards.filter { a -> a.selected.value == true && it.board != a.board }.size} and ${it.size} and ${it.board?.name}")
//                            intersectId.value = 0L
                                it.selected.value = false
                            }
                        }
                    }
//                println("ONDRAG=${offset}")

                },
                onDragEnd = {
//                println("END")
                    // Переместить с заменой parent
//                println(listOffsetsBoards.toString())
                    if (listOffsetsBoards.filter { it.selected.value == true }.size >= 1) {
                        // Доска не может быть перемещена к своим детям
                        viewModel.updateBoard(
                            BoardModel(
                                board.let { if (it != null) it.id else 0 },
                                board?.name.orEmpty(),
                                board?.date.orEmpty(),
                                listOffsetsBoards
                                    .filter { it.selected.value == true }
                                    .lastOrNull()!!.board!!.id
                            )
                        )
                        if (screenHierarchyState == Screen.Hierarchy) {
                            viewModel.setScreenHierarchyState(Screen.Hierarchy2)
                        } else {
                            viewModel.setScreenHierarchyState(Screen.Hierarchy)
                        }
                    } else {
                        // Сбросить назад
                        if (screenHierarchyState == Screen.Hierarchy) {
                            viewModel.setScreenHierarchyState(Screen.Hierarchy2)
                        } else {
                            viewModel.setScreenHierarchyState(Screen.Hierarchy)
                        }
                    }

                })
        }
        .combinedClickable(
            onClick = {
                viewModel.setCurrentBoardId(board!!.id)
            },
            onLongClick = {
                viewModel.setCurrentBoardId(board!!.id)
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
            tint = Color.Red,
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = board?.name.orEmpty(),
            color = Color.Black
        )
        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                viewModel.setOpenDialogAdding(true)
            }) {
                Text("Добавить")
            }
            DropdownMenuItem(onClick = {
                expanded = false
                viewModel.setOpenDialogEditing(true)
            }) {
                Text(text = "Редактировать")
            }
            if (board.let { if (it != null) it.id else 1L } != 1L) {
                DropdownMenuItem(onClick = {
                    if (allBoards.filter { it.id == board?.id }.first()
                            .let { if (it != null) it.id else 1L } != 1L
                    ) {
                        viewModel.deleteBoard(allBoards.filter { it.id == board?.id }.first())
                    }
                    expanded = false
                }) {
                    Text(text = "Удалить")
                }
            }
        }
    }
}


fun getAllParentsBoardIds(board:BoardModel?,listBoard: List<BoardModel>): List<Long>{
    var parentsBoard = mutableListOf<Long>()
    var parentBoard : BoardModel? = board
    parentsBoard.add(parentBoard!!.id)
    var i = 0
    while (i < listBoard.size){
        parentBoard = listBoard.filter { parentBoard!!.parent_id == it.id }.lastOrNull()
        parentsBoard.add(parentBoard!!.id)
        if (parentBoard!!.id == parentBoard!!.parent_id) break
        i++
    }
    return parentsBoard
}








