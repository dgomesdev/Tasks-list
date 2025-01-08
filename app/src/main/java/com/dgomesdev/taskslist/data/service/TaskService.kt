package com.dgomesdev.taskslist.data.service

import com.dgomesdev.taskslist.data.dto.request.TaskRequestDto
import com.dgomesdev.taskslist.data.dto.response.UserResponseDto

interface TaskService {
    suspend fun saveTask(task: TaskRequestDto): Result<UserResponseDto>
    suspend fun updateTask(taskId: String, task: TaskRequestDto): Result<UserResponseDto>
    suspend fun deleteTask(taskId: String): Result<UserResponseDto>
}