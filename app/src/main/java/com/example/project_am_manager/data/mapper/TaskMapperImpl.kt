package com.example.project_am_manager.data.mapper

import androidx.compose.ui.graphics.Color
import com.example.project_am_manager.data.database.model.TaskDbModel
import com.example.project_am_manager.domain.entity.TaskItem
import com.example.project_am_manager.domain.mapper.Mapper
import javax.inject.Inject


class TaskMapperImpl @Inject constructor(): Mapper<TaskItem,TaskDbModel> {
    override fun mapItemToDbModel(item: TaskItem): TaskDbModel {
        with(item){
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

    override fun mapDbModelToItem(dbModel: TaskDbModel): TaskItem {
        with(dbModel){
            return TaskItem(
                name,
                description,
                date,
                Color(android.graphics.Color.parseColor("#FFFFFF")),
                parent_id,
                id,
            )
        }
    }
}