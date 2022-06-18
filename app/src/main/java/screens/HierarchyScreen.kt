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
import com.example.project_am_manager.DataMain
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
    dataMain: DataMain
) {
//    BackButtonAction {
//        TManagerRouter.goBack()
//    }
//



    TransformableSample(dataMain)
}
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class, ExperimentalPagerApi::class)
@Composable
fun TransformableSample(
    dataMain: DataMain
) {
    val state_scroll_horizontal = rememberScrollState()
    val state_scroll_vertical = rememberScrollState()

    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        if (dataMain.scale_content.value*zoomChange >= 0.33){
            dataMain.scale_content.value *= zoomChange
            dataMain.offset_content.value += offsetChange
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .horizontalScroll(state_scroll_horizontal)
            .verticalScroll(state_scroll_vertical)
            .background(Color.White)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consumeAllChanges()
                    dataMain.offset_content.value += dragAmount
                }
            }
            .transformable(state = state)
            .offset { IntOffset((dataMain.offset_content.value.x).roundToInt(), (dataMain.offset_content.value.y).roundToInt()) }
            .graphicsLayer(
                scaleX = dataMain.scale_content.value,
                scaleY = dataMain.scale_content.value,
            )
    ){
        Box(modifier= Modifier
            .fillMaxSize()

        ) {

            val screenHierarchyState: MutableState<Screen> = remember {
                mutableStateOf(Screen.Hierarchy)
            }

            Column(modifier = Modifier
                .align(Alignment.Center)
                .background(Color.White)) {
                when(screenHierarchyState.value){
                    Screen.Hierarchy2 -> BuildingHierarchy(dataMain, screenHierarchyState)
                    Screen.Hierarchy -> BuildingHierarchy(dataMain, screenHierarchyState)
                    else -> {}
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun BuildingHierarchy(
    dataMain: DataMain,
    screenHierarchyState: MutableState<Screen>
){
    val allBoards: List<BoardModel> by dataMain.viewModel.allBoards.observeAsState(emptyList())


        Row() {
            println("RECREATE")
            val listOffsetsBoards: MutableList<boardBlock> = remember {
                mutableListOf()
            }

            Column(modifier = Modifier
                .align(Alignment.CenterVertically)
            ) {

                Board(dataMain.viewModel,allBoards.filter { it.id == it.parent_id }.firstOrNull(),dataMain.currentBoard,listOffsetsBoards,dataMain.stateModal,screenHierarchyState,dataMain.openDialogEditing)
            }
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {

                ColumnZ(allBoards.filter { it.id == it.parent_id }.firstOrNull().let { if (it != null) it.id else 0L } ,dataMain.viewModel,dataMain.currentBoard,listOffsetsBoards,dataMain.stateModal,screenHierarchyState,dataMain.openDialogEditing)

            }
        }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ColumnZ(
    idBoard: Long,
    viewModel: MainViewModel,
    currentBoard: MutableState<Long>,
    listOffsetsBoards: MutableList<boardBlock>,
    stateModal: ModalBottomSheetState,
    screenHierarchyState: MutableState<Screen>,
    openDialogEditing: MutableState<Boolean>
){
    if (idBoard == 0L) return
    // Найти всех детей и распарсить
    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
//    listBoards.value.filter { it }

    val endY = remember {
        mutableStateOf(0f)
    }
    // как-то получить координату первой и последней

    val startY = remember {
        mutableStateOf(0f)
    }

    allBoards.filter { it.parent_id == idBoard && it.id != it.parent_id}.forEachIndexed {index,it->

        Row(modifier = Modifier
            .background(Color.White)
            .onGloballyPositioned {
                if (index == allBoards.filter { it.parent_id == idBoard && it.id != it.parent_id }.size - 1) startY.value =
                    it.positionInParent().y
            }
            .drawBehind {

                println("index=$index drawBehind size.height=${size.height}")
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
            Column(modifier = Modifier
                .align(Alignment.CenterVertically)
) {
                Board(viewModel,it,currentBoard,listOffsetsBoards,stateModal,screenHierarchyState,openDialogEditing)
            }
            Column(modifier = Modifier.align(Alignment.CenterVertically)
) {
                ColumnZ(it.id,viewModel,currentBoard,listOffsetsBoards,stateModal,screenHierarchyState,openDialogEditing)

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
    currentBoard: MutableState<Long>,
    listOffsetsBoards: MutableList<boardBlock>,
    stateModal: ModalBottomSheetState,
    screenHierarchyState: MutableState<Screen>,
    openDialogEditing: MutableState<Boolean>

){

    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
    var offset = remember{ mutableStateOf(Offset(0f,0f)) }

    val selected = remember { mutableStateOf(false) }
    val sizeState = remember { mutableStateOf(IntSize.Zero) }

    val scale = animateFloatAsState(if (selected.value) 1.2f else 1f)

    val offset_global = remember {
        mutableStateOf(Offset.Zero)
    }

    val openDialogAdding = remember { mutableStateOf(false)  }

    val nameBoardStateAdding = remember { mutableStateOf(TextFieldValue()) }
    val nameBoardStateEditing = remember { mutableStateOf(TextFieldValue()) }
    val scope = rememberCoroutineScope()

    AlertDialogAdding(openDialog = openDialogAdding, nameBoardState = nameBoardStateAdding, viewModel = viewModel,board)

    AlertDialogEditing(openDialog = openDialogEditing, nameBoardState = nameBoardStateEditing, viewModel = viewModel,board,currentBoard,stateModal,scope)


    var expanded by remember { mutableStateOf(false) }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false  },modifier = Modifier
    ) {

            DropdownMenuItem(onClick = {
                expanded = false
                openDialogAdding.value = true

            }){
                Text("Добавить")
            }
        DropdownMenuItem(onClick = {
            expanded = false
            openDialogEditing.value = true
        }){
            Text(text = "Редактировать")
        }
        if (board.let { if(it!=null) it.id else 1L} != 1L){
            DropdownMenuItem(onClick = {
                if (allBoards.filter { it.id == board?.id }.first().let { if(it != null) it.id else 1L } != 1L){
                    viewModel.deleteBoard(allBoards.filter { it.id == board?.id }.first())
                }
                expanded = false
            }){
                Text(text = "Удалить")
            }
        }


    }
    val requestDisallowInterceptTouchEvent = RequestDisallowInterceptTouchEvent()
    requestDisallowInterceptTouchEvent.invoke(true)

    var motionEvent by remember { mutableStateOf(Recomposer.State.Idle) }


//    val pressed = remember {
//        mutableStateOf(true)
//    }
    val pressuredState= remember {
        mutableStateOf(1f)
    }

    val offsetDrag = remember {
        mutableStateOf(0f)
    }

//    val intersectId = remember {
//        mutableStateOf(0L)
//    }


Column (modifier= Modifier

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
                            println("ПЕРЕСЕКАЮТСЯ id ( ${board?.id} и ${it.board?.id}) __ it.off=${it.offset.value} off=${offset_global.value}")
                            // Вызвать эффект
                            // Обработать если отпущено

//                            intersectId.value = it.board.let { if (it != null) it.id else 0L }
//                            println("***intersectId=${intersectId.value}")
                            it.selected.value = true
//                            return@forEach


                        } else {
                            println("**${listOffsetsBoards.filter { a -> a.selected.value == true && it.board != a.board }.size} and ${it.size} and ${it.board?.name}")
//                            intersectId.value = 0L
                            it.selected.value = false
                        }
                    }
                }
                println("ONDRAG=${offset}")

            },
            onDragEnd = {
                println("END")
                // Переместить с заменой parent
                println(listOffsetsBoards.toString())

                if (listOffsetsBoards.filter { it.selected.value == true }.size >= 1) {
                    // Доска не может быть перемещена к своим детям
                    println(
                        "${board} and intersectionId=${
                            listOffsetsBoards
                                .filter { it.selected.value == true }
                                .lastOrNull()!!.board!!.id
                        }"
                    )

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
                    if (screenHierarchyState.value == Screen.Hierarchy) {
                        screenHierarchyState.value = Screen.Hierarchy2
                    } else {
                        screenHierarchyState.value = Screen.Hierarchy
                    }
                } else {
                    // Сбросить назад
                    if (screenHierarchyState.value == Screen.Hierarchy) {
                        screenHierarchyState.value = Screen.Hierarchy2
                    } else {
                        screenHierarchyState.value = Screen.Hierarchy
                    }
                }

            })

    }
    .combinedClickable(
        onClick = {
            currentBoard.value = board!!.id
        },
        onLongClick = {
            expanded = true
        },
    )
    .padding(15.dp)


){
    Icon(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_history_24),
        contentDescription = "",
        tint = Color.Black
        )
    Text(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        text = board?.name.orEmpty() ,
        color=Color.Black)


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








