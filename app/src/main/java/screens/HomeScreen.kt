package screens

import android.content.Intent
import android.text.Layout
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.project_am_manager.DataMain
import com.example.project_am_manager.MainActivity
import com.example.project_am_manager.R
import com.google.accompanist.pager.*
import domain.model.BoardModel
import domain.model.TaskModel
import kotlinx.coroutines.launch
import routing.BackButtonAction
import routing.TManagerRouter
import com.example.project_am_manager.TaskActivity
import viewmodel.MainViewModel
import java.text.SimpleDateFormat

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    dataMain: DataMain
) {
    val allBoards: List<BoardModel> by dataMain.viewModel.allBoards.observeAsState(emptyList())
    val allTasks: List<TaskModel> by dataMain.viewModel.allTasks.observeAsState(emptyList())
    val scope = rememberCoroutineScope()

//    BackButtonAction {
//        TManagerRouter.goBack()
//    }
    println("WTFFF")

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = dataMain.pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(dataMain.pagerState, tabPositions)
                )
            },
            ) {
                allBoards.filter { a ->
                    (a.parent_id == dataMain.boardParentId.value && !allTasks.filter { it.parent_id == a.id }
                        .isNullOrEmpty())
                }.forEachIndexed { index, board ->
                    Tab(
                        text = {
                            Text(text = board.name, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
                               },
                        selected = dataMain.pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                dataMain.pagerState.animateScrollToPage(index)

                            }
                        }, selectedContentColor = Color.Black, unselectedContentColor = Color.LightGray)
                }
        }
        HorizontalPager(count = allBoards.filter {a->  (a.parent_id == dataMain.boardParentId.value && !allTasks.filter{ it.parent_id == a.id } .isNullOrEmpty())  }.size, state = dataMain.pagerState, modifier = Modifier.fillMaxSize()) { page ->
            dataMain.currentBoard.value = allBoards.filter {a->  (a.parent_id == dataMain.boardParentId.value && !allTasks.filter{ it.parent_id == a.id } .isNullOrEmpty()) }.elementAtOrNull(page).let { if (it != null) it.id else 1 }
            LazyColumn(modifier = Modifier.fillMaxHeight()){
                itemsIndexed(items=allTasks.filter {
                    it.parent_id == allBoards.filter {a->  (a.parent_id == dataMain.boardParentId.value && !allTasks.filter{ it.parent_id == a.id } .isNullOrEmpty()) }.elementAtOrNull(page).let { if (it != null) it.id else 1 } },
                    itemContent = { index,item->
                    ToastContent(dataMain,item)
//                        dataMain.currentBoard.value = allBoards.filter {a->  (a.parent_id == dataMain.boardParentId.value && !allTasks.filter{ it.parent_id == a.id } .isNullOrEmpty()) }.elementAtOrNull(page).let { if (it != null) it.id else 1 }
                        if (index == allTasks.filter {
                                it.parent_id == allBoards.filter {a->  (a.parent_id == dataMain.boardParentId.value && !allTasks.filter{ it.parent_id == a.id } .isNullOrEmpty())  }.get(page).id }.size-1){
                            Spacer(modifier = Modifier.fillMaxWidth().height(60.dp))
                        }
                })
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun ToastContent(dataMain: DataMain,item: TaskModel) {
    val shape = RoundedCornerShape(4.dp)
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp,horizontal = 15.dp)
            .clip(shape)
            .background(Color(android.graphics.Color.parseColor("#F0F0F0")))

            .height(100.dp)
            .combinedClickable(
                onClick = {
                    val intent = Intent(context, TaskActivity::class.java)
                    intent.putExtra(MainActivity.TRANSMITTED_ID, item.id)
                    intent.putExtra(MainActivity.CURRENT_BOARD, dataMain.currentBoard.value)
                    context.startActivity(intent)

                },
                onLongClick = {
                    expanded = if (expanded) false else true
                }
            )
    ) {
        Column() {
            Text(text = item.name, color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.W500, modifier = Modifier.padding(10.dp),
            maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = item.description,color=Color.DarkGray, fontWeight = FontWeight.W400, modifier = Modifier
                .alpha(0.8f)
                .padding(PaddingValues(horizontal = 10.dp)),
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                , horizontalArrangement = Arrangement.End
            ) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_time_24), contentDescription = "",tint=Color.Black, modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .alpha(0.6f)
                    .size(18.dp)
                    .padding(top = 4.dp)
                )
                Text(text = item.date, textAlign = TextAlign.Right, fontWeight = FontWeight.W200, fontSize = 14.sp, color = Color.Black)
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false  },modifier = Modifier
        ) {
            DropdownMenuItem(onClick = {
                dataMain.viewModel.deleteTask(item)
                expanded = false
            },
            modifier = Modifier
                .clip(shape)
                .padding(0.dp)

                ){
                Text("Удалить", color = Color.Black)
            }
        }
        
    }

}