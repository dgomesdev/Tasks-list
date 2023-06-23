package com.dgomesdev.taskslist.data

import com.dgomesdev.taskslist.domain.TaskEntity
import com.dgomesdev.taskslist.domain.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override suspend fun getTasks() = taskDao.getTasks()

    override suspend fun saveTask(task: TaskEntity) {
        taskDao.saveTask(task)
    }

    override suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    override suspend fun deleteTask(vararg task: TaskEntity) {
        taskDao.deleteTask(*task)
    }

}