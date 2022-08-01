package com.example.project_am_manager.presentation.ui.compose.modalbottomsheet

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.project_am_manager.presentation.ui.compose.components.cards.BoardsScreen
import com.example.project_am_manager.presentation.viewmodel.IGuaranteeViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheetChooseBoard(
    viewModel: IGuaranteeViewModel,
    isEdit: Boolean = false,
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