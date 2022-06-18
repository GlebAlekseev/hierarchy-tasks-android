package components.modalbottomsheet

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.project_am_manager.DataMain
import com.example.project_am_manager.EditInputs
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.skyyo.expandablelist.cards.BoardsScreen
import kotlinx.coroutines.CoroutineScope
import viewmodel.MainViewModel


@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun ModalBottomSheetChoose(
    dataMain: DataMain? = null,
    editInputs: EditInputs? = null
) {

    ModalBottomSheetLayout(
        sheetState = if (dataMain != null){dataMain.state} else {editInputs!!.stateModal} ,
        sheetContent = {
            if (dataMain != null){
                BoardsScreen(dataMain)
            }else if (editInputs != null){
                BoardsScreen(editInputs)
            }
        }
    ) {


    }


}