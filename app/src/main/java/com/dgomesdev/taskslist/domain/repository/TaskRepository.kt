package com.dgomesdev.taskslist.domain.repository

import com.dgomesdev.taskslist.data.dto.request.TaskRequestDto
import com.dgomesdev.taskslist.data.dto.response.UserResponseDto
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun saveTask(task: TaskRequestDto): Flow<UserResponseDto>
    suspend fun updateTask(taskId: String, task: TaskRequestDto): Flow<UserResponseDto>
    suspend fun deleteTask(taskId: String): Flow<UserResponseDto>
}