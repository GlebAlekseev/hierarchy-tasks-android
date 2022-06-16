package dependencyinjection

import android.content.Context
import androidx.room.Room
import data.database.AppDatabase
import data.database.dbmapper.DbMapperBoard
import data.database.dbmapper.DbMapperBoardImpl
import data.database.dbmapper.DbMapperTask
import data.database.dbmapper.DbMapperTaskImpl
import data.repository.RepositoryBoard
import data.repository.RepositoryBoardImpl
import data.repository.RepositoryTask
import data.repository.RepositoryTaskImpl

class DependencyInjector(applicationContext: Context) {

    val repositoryTask: RepositoryTask by lazy { provideRepositoryTask(database) }
    val repositoryBoard: RepositoryBoard by lazy { provideRepositoryBoard(database) }

    private val database: AppDatabase by lazy { provideDatabase(applicationContext) }
    private val dbMapperBoard: DbMapperBoard = DbMapperBoardImpl()
    private val dbMapperTask: DbMapperTask = DbMapperTaskImpl()

    private fun provideDatabase(applicationContext: Context): AppDatabase =
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).allowMainThreadQueries().build()

    private fun provideRepositoryTask(database: AppDatabase): RepositoryTask {
        val taskDao = database.taskDao()
        return RepositoryTaskImpl(taskDao, dbMapperTask)
    }

    private fun provideRepositoryBoard(database: AppDatabase): RepositoryBoard {
        val boardDao = database.boardDao()
        return RepositoryBoardImpl(boardDao, dbMapperBoard)
    }
}