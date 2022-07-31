package com.example.project_am_manager.di.module

import com.example.project_am_manager.data.repositoryImpl.BoardListRepositoryImpl
import com.example.project_am_manager.data.repositoryImpl.TaskListRepositoryImpl
import com.example.project_am_manager.presentation.viewmodel.MainViewModelFactory
import com.example.project_am_manager.presentation.viewmodel.TaskViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {

    @Provides
    fun provideMainViewModelFactory(
        taskListRepository: TaskListRepositoryImpl,
        boardListRepository: BoardListRepositoryImpl
    ): MainViewModelFactory{
        return MainViewModelFactory(taskListRepository, boardListRepository)
    }

    @Provides
    fun provideTaskViewModelFactory(
        taskListRepository: TaskListRepositoryImpl,
        boardListRepository: BoardListRepositoryImpl
    ): TaskViewModelFactory{
        return TaskViewModelFactory(taskListRepository, boardListRepository)
    }

}