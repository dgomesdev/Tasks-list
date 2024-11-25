package com.dgomesdev.taskslist.data.dto.request

import com.dgomesdev.taskslist.domain.model.Priority
import com.dgomesdev.taskslist.domain.model.Status
import com.dgomesdev.taskslist.domain.model.Task
import kotlinx.serialization.Serializable

@ConsistentCopyVisibility
@Serializable
data class TaskRequestDto private constructor(
    val title: String,
    val description: String,
    val priority: Priority,
    val status: Status,
) {
    companion object {
        fun create(task: Task) =
            TaskRequestDto(task.title, task.description, task.priority, task.status)
    }
}