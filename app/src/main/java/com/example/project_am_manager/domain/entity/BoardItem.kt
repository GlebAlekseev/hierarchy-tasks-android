package com.example.project_am_manager.domain.entity


data class BoardItem(
    val name: String,
    val date: String,
    val parent_id: Long,
    val id: Long = UNDEFINED_ID
){
    companion object{
        const val UNDEFINED_ID = 0L
    }
}