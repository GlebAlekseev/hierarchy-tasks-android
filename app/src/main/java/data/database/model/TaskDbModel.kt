package data.database.model

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.project_am_manager.R
import java.text.SimpleDateFormat
import java.util.*


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
){
    companion object{
        val HELPER_TASKS = arrayOf(
            TaskDbModel(
                1,
                "Заметка 1",
                "Небольшое описание",
                SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()),
                "#FFFFFF",
                2
            ),
            TaskDbModel(
                2,
                "Заметка 2",
                "Небольшое описание",
                SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()),
                "#FFFFFF",
                2
            ),
            TaskDbModel(
                3,
                "Заметка 3",
                "Небольшое описание",
                SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()),
                "#FFFFFF",
                2
            ),
            TaskDbModel(
                4,
                "Заметка 4",
                "Небольшое описание",
                SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()),
                "#FFFFFF",
                2
            ),
            TaskDbModel(
                5,
                "Заметка 5",
                "Небольшое описание",
                SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()),
                "#FFFFFF",
                2
            ),
            TaskDbModel(
                6,
                "Заметка 6",
                "Небольшое описание",
                SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()),
                "#FFFFFF",
                2
            ),
        )



    }


}