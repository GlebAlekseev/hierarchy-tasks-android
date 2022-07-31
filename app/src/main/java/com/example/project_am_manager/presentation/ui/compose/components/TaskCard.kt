package com.example.project_am_manager.presentation.ui.compose.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_am_manager.R
import com.example.project_am_manager.domain.entity.TaskItem
import com.example.project_am_manager.presentation.activity.TaskActivity
import com.example.project_am_manager.presentation.viewmodel.MainViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(viewModel: MainViewModel, taskItem: TaskItem) {
    val shape = RoundedCornerShape(4.dp)
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val currentBoardId by viewModel.currentBoardId.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 15.dp)
            .clip(shape)
            .border(
                1.dp,
                Color.Black,
                shape = RoundedCornerShape(1.dp)
            )
            .background(Color(android.graphics.Color.parseColor("#F0F0F0")))
            .height(100.dp)
            .combinedClickable(
                onClick = {
                    val intent = TaskActivity.newIntentEditTask(context, taskItem.id, currentBoardId)
                    context.startActivity(intent)
                },
                onLongClick = {
                    expanded = if (expanded) false else true
                }
            )
    ) {
        Column() {
            Text(
                text = taskItem.name,
                color = Color(101, 160, 247),
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(10.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = taskItem.description,
                color = Color.DarkGray,
                fontWeight = FontWeight.W400,
                modifier = Modifier
                    .alpha(0.8f)
                    .padding(PaddingValues(horizontal = 10.dp)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp), horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_time_24),
                    contentDescription = "",
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .alpha(0.6f)
                        .size(18.dp)
                        .padding(top = 4.dp)
                )
                Text(
                    text = taskItem.date,
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.W200,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier
        ) {
            DropdownMenuItem(
                onClick = {
                    viewModel.deleteTask(taskItem)
                    expanded = false
                },
                modifier = Modifier
                    .clip(shape)
                    .padding(0.dp)
            ) {
                Text("Удалить", color = Color(53, 156, 252))
            }
        }
    }
}