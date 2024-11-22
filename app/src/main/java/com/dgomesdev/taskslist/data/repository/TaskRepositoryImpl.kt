package com.dgomesdev.taskslist.data.repository

import com.dgomesdev.taskslist.data.dto.request.TaskRequestDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.TaskResponseDto
import com.dgomesdev.taskslist.data.service.TaskService
import com.dgomesdev.taskslist.domain.repository.TaskRepository
import java.util.UUID

class TaskRepositoryImpl(private val taskService: TaskService) : TaskRepository {
    override suspend fun saveTask(task: TaskRequestDto): TaskResponseDto =
        taskService.saveTask(task)

    override suspend fun getTask(taskId: UUID): TaskResponseDto =
        taskService.getTask(taskId)

    override suspend fun updateTask(taskId: UUID, task: TaskRequestDto): TaskResponseDto =
        taskService.updateTask(taskId, task)

    override suspend fun deleteTask(taskId: UUID): MessageDto =
        taskService.deleteTask(taskId)
}