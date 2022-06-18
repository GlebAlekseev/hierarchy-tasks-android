package com.skyyo.expandablelist.cards

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.project_am_manager.DataMain
import com.example.project_am_manager.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import domain.model.BoardModel
import domain.model.TaskModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import com.example.project_am_manager.EditInputs
import viewmodel.MainViewModel

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalCoroutinesApi
@Composable
fun BoardsScreen(
    editInputs: EditInputs
) {
    val allBoards: List<BoardModel> by editInputs.viewModel.allBoards.observeAsState(emptyList())
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
                a.id==b.parent_id }.isNullOrEmpty()
            }) { _, board ->
                ExpandableCard(
                    editInputs = editInputs,
                    onCardArrowClick = { editInputs.viewModel.onBoardArrowClicked(board.id) },
                    expanded = expandedCardIds.value.contains(board.id),
                    board = board
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@ExperimentalCoroutinesApi
@Composable
fun BoardsScreen(
    dataMain: DataMain
) {
    val allBoards: List<BoardModel> by dataMain.viewModel.allBoards.observeAsState(emptyList())
    val allTasks: List<TaskModel> by dataMain.viewModel.allTasks.observeAsState(emptyList())
    val expandedCardIds = dataMain.viewModel.expandedBoardIdsList.collectAsState()

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
                a.id==b.parent_id && !allTasks.filter { b.id == it.parent_id }.isNullOrEmpty() }.isNullOrEmpty()
            }) { _, board ->
                ExpandableCard(
                    dataMain = dataMain,
                    onCardArrowClick = { dataMain.viewModel.onBoardArrowClicked(board.id) },
                    expanded = expandedCardIds.value.contains(board.id),
                    board = board
                )
            }
        }
    }
}




@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
    dataMain: DataMain? = null,
    editInputs: EditInputs? = null,
    onCardArrowClick: () -> Unit,
    expanded: Boolean,
    board: BoardModel
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
        if (expanded) Color.White else Color.Black
    }
    val cardColor by transition.animateColor({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "colorTransition") {
        if (expanded) Color.Black else Color.White
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
        contentColor = cardColor,
//        elevation = cardElevation,
//        shape = RoundedCornerShape(cardRoundedCorners),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 8.dp
            )
    ) {
        Column {

            if(editInputs != null){
                Box {
                    CardArrow(
                        degrees = arrowRotationDegree,
                        onClick = onCardArrowClick
                    )
                    CardTitle(title = board.name)
                }
                ExpandableContent(editInputs = editInputs,visible = expanded,board = board)
            }else if(dataMain != null){
                Box {
                    CardArrow(
                        degrees = arrowRotationDegree,
                        onClick = onCardArrowClick
                    )
                    CardTitle(title = board.name)
                }
                ExpandableContent(dataMain= dataMain,visible = expanded,board = board)
            }


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
            .padding(16.dp)
        ,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp

    )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun ExpandableContent(
    board:BoardModel,
    dataMain: DataMain? = null,
    editInputs: EditInputs? = null,
    visible: Boolean = true,

) {
    val allBoards: List<BoardModel> by
    if (editInputs != null) editInputs.viewModel.allBoards.observeAsState(emptyList()) else dataMain!!.viewModel.allBoards.observeAsState(emptyList())

    val allTasks: List<TaskModel> by
    if (editInputs != null) editInputs.viewModel.allTasks.observeAsState(emptyList()) else dataMain!!.viewModel.allTasks.observeAsState(emptyList())


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
    val shape = RoundedCornerShape(5.dp)
    AnimatedVisibility(
        visible = visible,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        Column(modifier = Modifier.padding(8.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            allBoards.filter {a-> a.parent_id == board.id  &&
                    if (editInputs != null) {!allTasks.filter { a.id == it.parent_id  }.isNullOrEmpty()}else true}.forEach{

                Box(modifier = Modifier
                    .clickable {
                    if  (dataMain != null) dataMain.currentBoard.value = it.id
                    scope.launch {
                        if (dataMain != null){
                            dataMain.state.hide()
                            if (dataMain.index_toAnimateGo.value != -1){
                                dataMain.pagerState.animateScrollToPage(dataMain.index_toAnimateGo.value)
                            }
                        }else if(editInputs != null){
                            editInputs.stateModal.hide()
                        }
                    }
                }
                    .padding(5.dp)
                    .border(BorderStroke(3.dp, Color.Black),shape)

                    .padding(10.dp)
                ){
                    Text(text = it.name, modifier = Modifier)
                }

            }
        }
    }
}



