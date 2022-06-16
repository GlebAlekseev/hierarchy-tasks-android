package data.database.model

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.project_am_manager.R


@Entity(foreignKeys = arrayOf(
    ForeignKey(
        entity = BoardDbModel::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("parent_id"),
        onDelete = CASCADE,
        onUpdate = CASCADE
    )
)
)
//@Entity
data class TaskDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "color") val color: String,
    @ColumnInfo(name = "parent_id") val parent_id: Long
)