package data.database.dbmapper

import data.database.model.BoardDbModel
import domain.model.BoardModel


interface DbMapperBoard {

    fun map(dbBoardDbModel: BoardDbModel): BoardModel
    fun mapDb(boardModel: BoardModel): BoardDbModel
}