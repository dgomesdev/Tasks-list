package com.dgomesdev.taskslist.domain.repository

import com.dgomesdev.taskslist.data.dto.request.TaskRequestDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.TaskResponseDto
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun saveTask(task: TaskRequestDto): Flow<TaskResponseDto>
    suspend fun getTask(taskId: String): Flow<TaskResponseDto>
    suspend fun updateTask(taskId: String, task: TaskRequestDto): Flow<TaskResponseDto>
    suspend fun deleteTask(taskId: String): Flow<MessageDto>
}