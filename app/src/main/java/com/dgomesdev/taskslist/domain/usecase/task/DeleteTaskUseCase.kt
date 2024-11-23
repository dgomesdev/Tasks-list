package com.dgomesdev.taskslist.domain.usecase.task

import com.dgomesdev.taskslist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.single

class DeleteTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: String): String =
        taskRepository.deleteTask(taskId).single().message
}