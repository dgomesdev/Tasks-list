package com.dgomesdev.taskslist

import android.app.Application
import com.dgomesdev.taskslist.data.local.TaskDatabase
import com.dgomesdev.taskslist.data.repository.TaskRepositoryImpl

class TasksApplication : Application() {

    private val tasksDatabase by lazy { TaskDatabase.getDatabase(this) }
    val tasksRepository by lazy { TaskRepositoryImpl(tasksDatabase.taskDao()) }
}