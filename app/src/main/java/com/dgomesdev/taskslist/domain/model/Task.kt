package com.dgomesdev.taskslist.domain.model

import com.dgomesdev.taskslist.data.dto.response.TaskResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val taskId: String = "",
    val title: String,
    val description: String,
    val priority: Priority,
    val status: Status,
) {
    companion object {
        fun fromApi(task: TaskResponseDto) = Task(
            taskId = task.taskId,
            title = task.title,
            description = task.description,
            priority = task.priority,
            status = task.status
        )
    }
}