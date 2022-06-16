package data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import data.database.dao.BoardDao
import data.database.dao.TaskDao
import data.database.model.BoardDbModel
import data.database.model.TaskDbModel

@Database(entities = [BoardDbModel::class,TaskDbModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "tmanager-database"
    }

    abstract fun taskDao(): TaskDao
    abstract fun boardDao(): BoardDao
}