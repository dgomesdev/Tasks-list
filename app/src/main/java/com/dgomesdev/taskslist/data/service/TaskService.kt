package com.dgomesdev.taskslist.data.service

import com.dgomesdev.taskslist.data.dto.request.TaskRequestDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.TaskResponseDto
import java.util.UUID

interface TaskService {
    fun saveTask(task: TaskRequestDto): TaskResponseDto
    fun getTask(taskId: UUID): TaskResponseDto
    fun updateTask(taskId: UUID, task: TaskRequestDto): TaskResponseDto
    fun deleteTask(taskId: UUID): MessageDto
}