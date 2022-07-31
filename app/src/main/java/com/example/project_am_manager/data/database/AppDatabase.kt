package com.example.project_am_manager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.project_am_manager.data.database.dao.BoardDao
import com.example.project_am_manager.data.database.dao.TaskDao
import com.example.project_am_manager.data.database.model.BoardDbModel
import com.example.project_am_manager.data.database.model.TaskDbModel

@Database(entities = [BoardDbModel::class, TaskDbModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "tmanager-database"
        fun getDataBase(context: Context): AppDatabase{
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).allowMainThreadQueries().build()
        }
    }

    abstract fun taskDao(): TaskDao
    abstract fun boardDao(): BoardDao
}