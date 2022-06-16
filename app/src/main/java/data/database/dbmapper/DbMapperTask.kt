package data.database.dbmapper

import data.database.model.BoardDbModel
import data.database.model.TaskDbModel
import domain.model.BoardModel
import domain.model.TaskModel

interface DbMapperTask {

    fun map(dbTaskDbModel: TaskDbModel): TaskModel
    fun mapDb(taskModel: TaskModel): TaskDbModel
}