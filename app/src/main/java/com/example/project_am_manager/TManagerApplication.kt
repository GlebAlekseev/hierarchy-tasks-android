package com.example.project_am_manager

import android.app.Application
import dependencyinjection.DependencyInjector

class TManagerApplication: Application() {
    lateinit var dependencyInjector: DependencyInjector


    override fun onCreate() {
        super.onCreate()
        initDependencyInjector()
    }

    private fun initDependencyInjector() {
        dependencyInjector = DependencyInjector(this)

    }
}