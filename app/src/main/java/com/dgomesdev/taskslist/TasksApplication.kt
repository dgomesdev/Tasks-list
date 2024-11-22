package com.dgomesdev.taskslist

import android.app.Application
import com.dgomesdev.taskslist.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TasksApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TasksApplication)
            modules(appModule)
        }
    }
}