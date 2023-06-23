package com.dgomesdev.taskslist.data

import android.app.Application
import androidx.room.Room
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

