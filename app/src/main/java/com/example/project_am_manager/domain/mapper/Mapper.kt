package com.example.project_am_manager.domain.mapper

interface Mapper<ITEM,DBMODEL> {
    fun mapItemToDbModel(item: ITEM): DBMODEL
    fun mapDbModelToItem(dbModel: DBMODEL): ITEM
}