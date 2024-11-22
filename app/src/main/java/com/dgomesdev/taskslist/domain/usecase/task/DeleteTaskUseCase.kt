package com.dgomesdev.taskslist.domain.usecase.task

import com.dgomesdev.taskslist.domain.repository.TaskRepository
import java.util.UUID

class DeleteTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: UUID): String =
        taskRepository.deleteTask(taskId).message
}