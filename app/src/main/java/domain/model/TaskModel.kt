package domain.model

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.project_am_manager.R
import java.text.SimpleDateFormat
import java.util.*


data class TaskModel(
    val id: Long,
    val name: String,
    val description: String,
    val date: String,
    val color: Color,
    val parent_id: Long,

)