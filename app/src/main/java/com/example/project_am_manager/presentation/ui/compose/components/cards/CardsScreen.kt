@file:OptIn(ExperimentalPagerApi::class)

package com.skyyo.expandablelist.cards

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.project_am_manager.R
import com.example.project_am_manager.domain.entity.BoardItem
import com.example.project_am_manager.domain.entity.TaskItem
import com.example.project_am_manager.presentation.viewmodel.IGuaranteeViewModel
import com.example.project_am_manager.presentation.viewmodel.MainViewModel
import com.example.project_am_manager.presentation.viewmodel.TaskViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@Composable
fun BoardsScreen(
    viewModel: IGuaranteeViewModel,
    isEdit: Boolean
) {
    val allBoards: List<BoardItem> by viewModel.getBoardList().observeAsState(emptyList())
    val expandedCardIds by viewModel.expandedBoardIdsList.collectAsState()
    val allTasks: List<TaskItem> by viewModel.getTaskList().observeAsState(emptyList())

    Scaffold(
        backgroundColor = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.colorDayNightWhite
            )
        )
    ) {
        LazyColumn {
            itemsIndexed(allBoards.filter { a ->
                !allBoards.filter { b ->
                    a.id == b.parent_id && if (!isEdit) {
                        !allTasks.filter { b.id == it.parent_id }.isNullOrEmpty()
                    } else true
                }.isNullOrEmpty()
            }) { _, board ->
                ExpandableCard(
                    viewModel = viewModel,
                    onCardArrowClick = { viewModel.onBoardArrowClicked(board.id) },
                    expanded = expandedCardIds.contains(board.id),
                    board = board,
                    isEdit
                )
            }
        }
    }
}


@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
    viewModel: IGuaranteeViewModel,
    onCardArrowClick: () -> Unit,
    expanded: Boolean,
    board: BoardItem,
    isEdit: Boolean
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
        if (expanded) Color.White else Color(62, 125, 250)
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
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
            ExpandableContent(
                viewModel = viewModel,
                visible = expanded,
                board = board,
                isEdit = isEdit
            )
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
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp

    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableContent(
    viewModel: IGuaranteeViewModel,
    board: BoardItem,
    visible: Boolean = true,
    isEdit: Boolean

) {
    val allBoards: List<BoardItem> by viewModel.getBoardList().observeAsState(emptyList())
    val allTasks: List<TaskItem> by viewModel.getTaskList().observeAsState(emptyList())
    val stateModal by viewModel.stateModal.collectAsState()
    val indexAnimateTarget by viewModel.indexAnimateTarget.collectAsState()
    val pagerState by viewModel.pagerState.collectAsState()
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
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            allBoards.filter { a ->
                a.parent_id == board.id &&
                        if (!isEdit) {
                            !allTasks.filter { a.id == it.parent_id }.isNullOrEmpty()
                        } else true
            }.forEach {

                Box(modifier = Modifier
                    .clickable {
                        if (isEdit && viewModel is TaskViewModel) viewModel.setTransmittedParentId(it.id)
                        scope.launch {
                            if (!isEdit && viewModel is MainViewModel) {
                                stateModal.hide()
                                viewModel.setCurrentBoardId(it.id)
                                viewModel.refreshIndexAnimateTarget()
                                if (indexAnimateTarget != -1) {
                                    pagerState.animateScrollToPage(indexAnimateTarget)
                                    pagerState.animateScrollToPage(indexAnimateTarget)
                                }
                            } else if (isEdit) {
                                    viewModel.stateModal.value.hide()
                            }
                        }
                    }
                    .padding(5.dp)
                    .border(BorderStroke(3.dp, Color(66, 142, 255)), shape)
                    .fillMaxWidth()
                    .padding(10.dp)
                ) {
                    Row() {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_bookmark_24),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                        Text(text = it.name, modifier = Modifier, fontWeight = FontWeight.W400)
                    }
                }
            }
        }
    }
}


