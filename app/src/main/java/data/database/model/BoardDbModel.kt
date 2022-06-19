package data.database.model

import android.provider.ContactsContract
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*


@Entity(foreignKeys = arrayOf(
    ForeignKey(
        entity = BoardDbModel::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("parent_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )
)
)
//@Entity
data class BoardDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "parent_id") val parent_id: Long
){
    companion object{
        val ROOT_BOARD =
            BoardDbModel(
                1,
                "Главная доска",
                SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()),
                1
            )
        val HELPER_BOARD =
            BoardDbModel(
                2,
                "Доска 1",
                SimpleDateFormat("dd:MM:yyyy hh:mm:ss").format(Date()),
                1
            )


    }

}
