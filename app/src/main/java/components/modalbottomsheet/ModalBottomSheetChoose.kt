package components.modalbottomsheet

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.skyyo.expandablelist.cards.BoardsScreen
import kotlinx.coroutines.CoroutineScope
import viewmodel.MainViewModel


@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun ModalBottomSheetChoose(
viewModel: MainViewModel,
isEdit:Boolean = false
) {
    val stateModalEdit by viewModel.stateModalEdit .collectAsState()
    val stateModalMain by viewModel.stateModalMain .collectAsState()

    ModalBottomSheetLayout(
        sheetState = if (isEdit){stateModalEdit} else {stateModalMain} ,
        sheetContent = {
                BoardsScreen(viewModel,isEdit)
        }
    ) {

    }
}