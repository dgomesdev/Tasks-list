package com.dgomesdev.taskslist.data.repository

import com.dgomesdev.taskslist.data.dto.request.TaskRequestDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.TaskResponseDto
import com.dgomesdev.taskslist.data.service.TaskService
import com.dgomesdev.taskslist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TaskRepositoryImpl(private val taskService: TaskService) : TaskRepository {
    override suspend fun saveTask(task: TaskRequestDto): Flow<TaskResponseDto> =
        flow { emit(taskService.saveTask(task).getOrElse { error(it) }) }

    override suspend fun updateTask(taskId: String, task: TaskRequestDto): Flow<TaskResponseDto> =
        flow { emit(taskService.updateTask(taskId, task).getOrElse { error(it) }) }

    override suspend fun deleteTask(taskId: String): Flow<MessageDto> =
        flow { emit(taskService.deleteTask(taskId).getOrElse { error(it) }) }
}