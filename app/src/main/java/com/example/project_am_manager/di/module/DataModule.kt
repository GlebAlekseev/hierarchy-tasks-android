package com.example.project_am_manager.di.module

import com.example.project_am_manager.data.database.AppDatabase
import com.example.project_am_manager.data.database.dao.BoardDao
import com.example.project_am_manager.data.database.dao.TaskDao
import com.example.project_am_manager.data.database.model.BoardDbModel
import com.example.project_am_manager.data.database.model.TaskDbModel
import com.example.project_am_manager.data.mapper.BoardMapperImpl
import com.example.project_am_manager.data.mapper.TaskMapperImpl
import com.example.project_am_manager.domain.entity.BoardItem
import com.example.project_am_manager.domain.entity.TaskItem
import com.example.project_am_manager.domain.mapper.Mapper
import dagger.Module
import dagger.Provides

@Module
object DataModule {
    @Provides
    fun provideBoardMapper(impl: BoardMapperImpl): Mapper<BoardItem, BoardDbModel>{
        return impl
    }

    @Provides
    fun provideTaskMapper(impl: TaskMapperImpl): Mapper<TaskItem, TaskDbModel>{
        return impl
    }

    @Provides
    fun provideBoardDao(impl: AppDatabase): BoardDao{
        return impl.boardDao()
    }

    @Provides
    fun provideTaskDao(impl: AppDatabase): TaskDao{
        return impl.taskDao()
    }
}