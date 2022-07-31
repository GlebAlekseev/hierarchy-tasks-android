package com.example.project_am_manager.di

import com.example.project_am_manager.di.module.*
import com.example.project_am_manager.presentation.activity.MainActivity
import com.example.project_am_manager.presentation.activity.TaskActivity
import dagger.Component

@Component(modules = [AppModule::class,DataModule::class,DomainModule::class,LocalBaseModule::class,PresentationModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: TaskActivity)
}
