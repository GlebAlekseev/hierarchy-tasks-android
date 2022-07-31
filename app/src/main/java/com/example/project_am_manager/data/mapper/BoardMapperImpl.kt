package com.example.project_am_manager.data.mapper

import com.example.project_am_manager.data.database.model.BoardDbModel
import com.example.project_am_manager.domain.entity.BoardItem
import com.example.project_am_manager.domain.mapper.Mapper
import javax.inject.Inject

class BoardMapperImpl @Inject constructor(): Mapper<BoardItem,BoardDbModel> {
    override fun mapItemToDbModel(item: BoardItem): BoardDbModel {
        with(item){
            return BoardDbModel(
                id,
                name,
                date,
                parent_id,
            )
        }
    }

    override fun mapDbModelToItem(dbModel: BoardDbModel): BoardItem {
        with(dbModel) {
            return BoardItem(
                name,
                date,
                parent_id,
                id,
            )
        }
    }
}