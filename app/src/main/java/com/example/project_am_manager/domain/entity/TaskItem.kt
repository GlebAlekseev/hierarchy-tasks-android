package com.example.project_am_manager.domain.entity

import androidx.compose.ui.graphics.Color


data class TaskItem(
    val name: String,
    val description: String,
    val date: String,
    val color: Color,
    val parent_id: Long,
    val id: Long = UNDEFINED_ID
){
    companion object{
        const val UNDEFINED_ID = 0L
    }
}