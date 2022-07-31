package com.example.project_am_manager.presentation.ui.compose.components.modalbottomsheet

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.example.project_am_manager.presentation.viewmodel.IGuaranteeViewModel
import com.example.project_am_manager.presentation.viewmodel.MainViewModel
import com.example.project_am_manager.presentation.viewmodel.TaskViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.skyyo.expandablelist.cards.BoardsScreen


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheetChoose(
    viewModel: IGuaranteeViewModel,
    isEdit: Boolean = false
) {
    val stateModal by viewModel.stateModal.collectAsState()
    ModalBottomSheetLayout(
        sheetState = stateModal,
        sheetContent = {
            BoardsScreen(viewModel, isEdit)
        }
    ) {
    }
}