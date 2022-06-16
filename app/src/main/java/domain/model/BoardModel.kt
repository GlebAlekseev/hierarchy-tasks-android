package domain.model

import com.example.project_am_manager.R


data class BoardModel(
    val id: Long,
    val name: String,
    val date: String,
    val parent_id: Long,
)