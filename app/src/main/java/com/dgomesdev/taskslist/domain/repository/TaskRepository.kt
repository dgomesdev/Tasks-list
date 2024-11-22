package com.dgomesdev.taskslist.domain.repository

import com.dgomesdev.taskslist.data.dto.request.TaskRequestDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.TaskResponseDto
import java.util.UUID

interface TaskRepository {
    suspend fun saveTask(task: TaskRequestDto): TaskResponseDto
    suspend fun getTask(taskId: UUID): TaskResponseDto
    suspend fun updateTask(taskId: UUID, task: TaskRequestDto): TaskResponseDto
    suspend fun deleteTask(taskId: UUID): MessageDto
}