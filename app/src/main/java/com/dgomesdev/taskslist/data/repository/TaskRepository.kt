package com.dgomesdev.taskslist.data.repository

import com.dgomesdev.taskslist.domain.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun getTasks(): Flow<List<TaskEntity>>

    suspend fun saveTask(task: TaskEntity)

    suspend fun updateTask(task: TaskEntity)

    suspend fun deleteTask(vararg task: TaskEntity)

}