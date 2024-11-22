package com.dgomesdev.taskslist.data.service.impl

import com.dgomesdev.taskslist.data.dto.request.TaskRequestDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.TaskResponseDto
import com.dgomesdev.taskslist.data.service.TaskService
import java.util.UUID

class TaskServiceImpl: TaskService {
    override fun saveTask(task: TaskRequestDto): TaskResponseDto {
        TODO("Not yet implemented")
    }

    override fun getTask(taskId: UUID): TaskResponseDto {
        TODO("Not yet implemented")
    }

    override fun updateTask(taskId: UUID, task: TaskRequestDto): TaskResponseDto {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: UUID): MessageDto {
        TODO("Not yet implemented")
    }
}