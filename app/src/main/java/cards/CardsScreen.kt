package com.skyyo.expandablelist.cards

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.project_am_manager.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import domain.model.BoardModel
import domain.model.TaskModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import screens.ToastContent
import screens.task.EditInputs
import viewmodel.MainViewModel

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalCoroutinesApi
@Composable
fun BoardsScreen(editInputs: EditInputs, isAll:Boolean) {

    val allBoards: List<BoardModel> by editInputs.viewModel.allBoards.observeAsState(emptyList())
    val allTasks: List<TaskModel> by editInputs.viewModel.allTasks.observeAsState(emptyList())
    val expandedCardIds = editInputs.viewModel.expandedBoardIdsList.collectAsState()

    Scaffold(
        backgroundColor = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.colorDayNightWhite
            )
        )
    ) {
        LazyColumn {
            itemsIndexed(allBoards.filter {a-> !allBoards.filter {b->
                a.id==b.parent_id && if (!isAll) !allTasks.filter { b.id == it.parent_id }.isNullOrEmpty() else true }.isNullOrEmpty()

            }) { _, board ->
                ExpandableCard(
                    state = editInputs.stateModal,
                    allTasks = allTasks,
                    allBoards = allBoards,
                    board = board,
                    onCardArrowClick = { editInputs.viewModel.onBoardArrowClicked(board.id) },
                    expanded = expandedCardIds.value.contains(board.id),
                    isAll = isAll,
                    editInputs.parentBoardState,

                )
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@ExperimentalCoroutinesApi
@Composable
fun BoardsScreen(
    index_toAnimateGo: MutableState<Int>,
    pagerState: PagerState,
    state: ModalBottomSheetState,
    viewModel: MainViewModel,
    isAll: Boolean,
    currentBoard: MutableState<Long>
) {
    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
    val allTasks: List<TaskModel> by viewModel.allTasks.observeAsState(emptyList())
    val expandedCardIds = viewModel.expandedBoardIdsList.collectAsState()

    Scaffold(
        backgroundColor = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.colorDayNightWhite
            )
        )
    ) {
        LazyColumn {
            itemsIndexed(allBoards.filter {a-> !allBoards.filter {b->
                a.id==b.parent_id && if (!isAll) !allTasks.filter { b.id == it.parent_id }.isNullOrEmpty() else true }.isNullOrEmpty()

            }) { _, board ->
                ExpandableCard(
                    state = state,
                    allTasks = allTasks,
                    allBoards = allBoards,
                    board = board,
                    onCardArrowClick = { viewModel.onBoardArrowClicked(board.id) },
                    expanded = expandedCardIds.value.contains(board.id),
                    isAll = isAll,
                    currentBoard,
                    pagerState,
                    index_toAnimateGo
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
    state: ModalBottomSheetState,
    allTasks: List<TaskModel>,
    allBoards: List<BoardModel>,
    board: BoardModel,
    onCardArrowClick: () -> Unit,
    expanded: Boolean,
    isAll: Boolean,
    currentBoard: MutableState<Long>,
    pagerState: PagerState = rememberPagerState(),
    index_toAnimateGo: MutableState<Int> = mutableStateOf(-1)

) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "transition")
    val cardBgColor by transition.animateColor({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "bgColorTransition") {
        if (expanded) Color.Cyan else Color.Gray
    }
    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "paddingTransition") {
        if (expanded) 48.dp else 24.dp
    }
    val cardElevation by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "elevationTransition") {
        if (expanded) 24.dp else 4.dp
    }
    val cardRoundedCorners by transition.animateDp({
        tween(
            durationMillis = EXPAND_ANIMATION_DURATION,
            easing = FastOutSlowInEasing
        )
    }, label = "cornersTransition") {
        if (expanded) 0.dp else 16.dp
    }
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0f else 180f
    }

    Card(
        backgroundColor = cardBgColor,
        contentColor = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.colorDayNightPurple
            )
        ),
        elevation = cardElevation,
        shape = RoundedCornerShape(cardRoundedCorners),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = cardPaddingHorizontal,
                vertical = 8.dp
            )
    ) {
        Column {
            Box {
                CardArrow(
                    degrees = arrowRotationDegree,
                    onClick = onCardArrowClick
                )
                CardTitle(title = board.name)
            }
            ExpandableContent(state=state,allTasks=allTasks,allBoards=allBoards,board=board,visible = expanded,isAll=isAll,currentBoard=currentBoard,pagerState,index_toAnimateGo)
        }
    }
}

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_expand_less_24),
                contentDescription = "Expandable Arrow",
                modifier = Modifier.rotate(degrees),
            )
        }
    )
}

@Composable
fun CardTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center,
    )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun ExpandableContent(
    state: ModalBottomSheetState,
    allTasks: List<TaskModel>,
    allBoards: List<BoardModel>,
    board: BoardModel,
    visible: Boolean = true,
    isAll: Boolean,
    currentBoard: MutableState<Long>,
    pagerState: PagerState,
    index_toAnimateGo:MutableState<Int>
) {
    val scope = rememberCoroutineScope()
    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = FADE_IN_ANIMATION_DURATION,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember {
        expandVertically(animationSpec = tween(EXPAND_ANIMATION_DURATION))
    }
    val exitFadeOut = remember {
        fadeOut(
            animationSpec = TweenSpec(
                durationMillis = FADE_OUT_ANIMATION_DURATION,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(COLLAPSE_ANIMATION_DURATION))
    }

    AnimatedVisibility(
        visible = visible,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            allBoards.filter {a-> a.parent_id == board.id  && if (!isAll) {!allTasks.filter { a.id == it.parent_id  }.isNullOrEmpty()}else true}.forEach{
                Text(text = it.name + it.date, modifier = Modifier.clickable {
                    currentBoard.value = it.id
                    scope.launch {
                        state.hide()

                        if (index_toAnimateGo.value != -1){
                            pagerState.animateScrollToPage(index_toAnimateGo.value)
                        }

                    }

                })
                
            }

        }
    }
}



@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@ExperimentalCoroutinesApi
@Composable
fun BoardsScreen(
    state: ModalBottomSheetState,
    viewModel: MainViewModel,
    currentBoard: MutableState<Long>,
    openDialog: MutableState<Boolean>
) {
    val allBoards: List<BoardModel> by viewModel.allBoards.observeAsState(emptyList())
    val allTasks: List<TaskModel> by viewModel.allTasks.observeAsState(emptyList())
    val expandedCardIds = viewModel.expandedBoardIdsList.collectAsState()

    Scaffold(
        backgroundColor = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.colorDayNightWhite
            )
        )
    ) {
        LazyColumn {
            itemsIndexed(allBoards.filter {a-> !allBoards.filter {b->
                a.id==b.parent_id }.isNullOrEmpty()

            }) { _, board ->
                ExpandableCard(
                    state = state,
                    allTasks = allTasks,
                    allBoards = allBoards,
                    board = board,
                    onCardArrowClick = { viewModel.onBoardArrowClicked(board.id) },
                    expanded = expandedCardIds.value.contains(board.id),
                    currentBoard,
                    openDialog
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
    state: ModalBottomSheetState,
    allTasks: List<TaskModel>,
    allBoards: List<BoardModel>,
    board: BoardModel,
    onCardArrowClick: () -> Unit,
    expanded: Boolean,
    currentBoard: MutableState<Long>,
    openDialog: MutableState<Boolean>

) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "transition")
    val cardBgColor by transition.animateColor({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "bgColorTransition") {
        if (expanded) Color.Cyan else Color.Gray
    }
    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "paddingTransition") {
        if (expanded) 48.dp else 24.dp
    }
    val cardElevation by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "elevationTransition") {
        if (expanded) 24.dp else 4.dp
    }
    val cardRoundedCorners by transition.animateDp({
        tween(
            durationMillis = EXPAND_ANIMATION_DURATION,
            easing = FastOutSlowInEasing
        )
    }, label = "cornersTransition") {
        if (expanded) 0.dp else 16.dp
    }
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0f else 180f
    }

    Card(
        backgroundColor = cardBgColor,
        contentColor = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.colorDayNightPurple
            )
        ),
        elevation = cardElevation,
        shape = RoundedCornerShape(cardRoundedCorners),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = cardPaddingHorizontal,
                vertical = 8.dp
            )
    ) {
        Column {
            Box {
                CardArrow(
                    degrees = arrowRotationDegree,
                    onClick = onCardArrowClick
                )
                CardTitle(title = board.name)
            }
            ExpandableContent(state=state,allTasks=allTasks,allBoards=allBoards,board=board,visible = expanded,currentBoard=currentBoard,openDialog)
        }
    }
}


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun ExpandableContent(
    state: ModalBottomSheetState,
    allTasks: List<TaskModel>,
    allBoards: List<BoardModel>,
    board: BoardModel,
    visible: Boolean = true,
    currentBoard: MutableState<Long>,
    openDialog: MutableState<Boolean>
) {
    val scope = rememberCoroutineScope()
    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = FADE_IN_ANIMATION_DURATION,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember {
        expandVertically(animationSpec = tween(EXPAND_ANIMATION_DURATION))
    }
    val exitFadeOut = remember {
        fadeOut(
            animationSpec = TweenSpec(
                durationMillis = FADE_OUT_ANIMATION_DURATION,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(COLLAPSE_ANIMATION_DURATION))
    }

    AnimatedVisibility(
        visible = visible,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            allBoards.filter {a-> a.parent_id == board.id }.forEach{
                Text(text = it.name + it.date, modifier = Modifier.clickable {
                    currentBoard.value = it.id
                    scope.launch {
                        state.hide()
                        openDialog.value = true
                    }

                })

            }

        }
    }
}


