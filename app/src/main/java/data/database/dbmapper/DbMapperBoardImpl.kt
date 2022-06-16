package data.database.dbmapper

import data.database.model.BoardDbModel
import domain.model.BoardModel


class DbMapperBoardImpl : DbMapperBoard {

    override fun map(dbBoardDbModel: BoardDbModel): BoardModel {
        with(dbBoardDbModel) {
            return BoardModel(
                id,
                name,
                date,
                parent_id,
            )
        }
    }

    override fun mapDb(boardModel: BoardModel): BoardDbModel {
        with(boardModel) {
            return BoardDbModel(
                id,
                name,
                date,
                parent_id,
            )
        }
    }

}