package com.dgomesdev.taskslist.data.service

import com.dgomesdev.taskslist.data.dto.request.TaskRequestDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.TaskResponseDto

interface TaskService {
    suspend fun saveTask(task: TaskRequestDto): Result<TaskResponseDto>
    suspend fun getTask(taskId: String): Result<TaskResponseDto>
    suspend fun updateTask(taskId: String, task: TaskRequestDto): Result<TaskResponseDto>
    suspend fun deleteTask(taskId: String): Result<MessageDto>
}