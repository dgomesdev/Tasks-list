package com.dgomesdev.taskslist.data.local.di

import android.app.Application
import androidx.room.Room
import com.dgomesdev.taskslist.data.local.TaskDao
import com.dgomesdev.taskslist.data.local.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDao(taskDatabase: TaskDatabase) : TaskDao = taskDatabase.taskDao()

    @Provides
    @Singleton
    fun provideDatabase(application: Application): TaskDatabase {
        return Room.databaseBuilder(
            application,
            TaskDatabase::class.java,
            "Task_database"
        ).build()
    }

}

