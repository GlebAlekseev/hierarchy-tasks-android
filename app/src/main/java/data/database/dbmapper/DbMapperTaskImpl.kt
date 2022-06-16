package data.database.dbmapper


import androidx.compose.ui.graphics.Color
import data.database.model.TaskDbModel
import domain.model.TaskModel


class DbMapperTaskImpl : DbMapperTask {

    override fun map(dbTaskDbModel: TaskDbModel): TaskModel {
        with(dbTaskDbModel) {
            return TaskModel(
                id,
                name,
                description,
                date,
                Color(android.graphics.Color.parseColor("#FFFFFF")),
                parent_id,

            )
        }
    }

    override fun mapDb(taskModel: TaskModel): TaskDbModel {
        with(taskModel) {
            return TaskDbModel(
                id,
                name,
                description,
                date,
                "#FFFFFF",
                parent_id,
            )
        }
    }


}