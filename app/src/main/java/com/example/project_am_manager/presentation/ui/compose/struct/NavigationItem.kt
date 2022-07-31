package com.example.project_am_manager.presentation.ui.compose.struct

import com.example.project_am_manager.R

data class NavigationItem(
    val index: Int,
    val vectorResourceId: Int,
    val contentDescriptionResourceId: Int,
    val screen: String
){
    companion object{
        val items = listOf(
            NavigationItem(0, R.drawable.ic_baseline_home_24, R.string.home, "Home"),
            NavigationItem(1, R.drawable.ic_baseline_hierarchy_24, R.string.hierarchy, "Hierarchy"),
            NavigationItem(2, R.drawable.ic_baseline_history_24, R.string.history, "History"),
        )
    }
}