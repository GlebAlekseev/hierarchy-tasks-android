package com.example.project_am_manager.di.module

import android.content.Context
import com.example.project_am_manager.data.database.AppDatabase
import com.example.project_am_manager.data.database.AppDatabase.Companion.getDataBase
import dagger.Module
import dagger.Provides

@Module
class LocalBaseModule {
    @Provides
    fun provideAppDataBase(context: Context): AppDatabase{
        return getDataBase(context)
    }
}