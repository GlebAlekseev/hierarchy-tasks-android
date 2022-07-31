package com.example.project_am_manager.presentation.application

import android.app.Application
import android.content.Context
import com.example.project_am_manager.di.AppComponent
import com.example.project_am_manager.di.DaggerAppComponent
import com.example.project_am_manager.di.module.AppModule


class MainApplication: Application() {
    val appComponent: AppComponent by lazy {
        println("this=>$this")
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
    }
}

val Context.appComponent: AppComponent
    get() = when(this){
        is MainApplication -> {
            println("is Applictaion -> $this")
            appComponent
        }
        else -> {
            println("else -> $this")
            this.applicationContext.appComponent
        }

    }