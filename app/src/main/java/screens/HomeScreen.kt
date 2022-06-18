package screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
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
                        .isNullOrEmpty()) && a.id != dataMain.boardParentId.value
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
        HorizontalPager(count = allBoards.filter {a->  (a.parent_id == dataMain.boardParentId.value && !allTasks.filter{ it.parent_id == a.id } .isNullOrEmpty()) && a.id != dataMain.boardParentId.value }.size, state = dataMain.pagerState, modifier = Modifier.fillMaxSize()) { page ->
            LazyColumn(modifier = Modifier.fillMaxHeight()){
                items(items=allTasks.filter {
                    it.parent_id == allBoards.filter {a->  (a.parent_id == dataMain.boardParentId.value && !allTasks.filter{ it.parent_id == a.id } .isNullOrEmpty()) && a.id != dataMain.boardParentId.value }.get(page).id },
                    itemContent = { item->
                    ToastContent(dataMain.viewModel,item)
                })
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToastContent(viewModel: MainViewModel,item: TaskModel) {
    val shape = RoundedCornerShape(4.dp)
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp,horizontal = 15.dp)
            .clip(shape)
            .background(Color.LightGray)
//            .border(1.dp, Color.Black, shape)
            .height(100.dp)
            .combinedClickable(
                onClick = {
                    val intent = Intent(context, TaskActivity::class.java)
                    intent.putExtra(MainActivity.TRANSMITTED_ID, item.id)
                    context.startActivity(intent)

                },
                onLongClick = {
                    expanded = if (expanded) false else true
                }
            )
    ) {
        Column() {
            Text(text = item.name, color = Color.Black)
            Text(text = item.description,color=Color.Black, modifier = Modifier.alpha(0.7f))
            Row(modifier = Modifier) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_time_24), contentDescription = "")
                Text(text = item.date, textAlign = TextAlign.Right)
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false  },modifier = Modifier
        ) {
            DropdownMenuItem(onClick = {
                viewModel.deleteTask(item)
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