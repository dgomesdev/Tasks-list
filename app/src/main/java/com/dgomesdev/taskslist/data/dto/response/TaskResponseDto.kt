package com.dgomesdev.taskslist.data.dto.response

import com.dgomesdev.taskslist.domain.model.Priority
import com.dgomesdev.taskslist.domain.model.Status
import kotlinx.serialization.Serializable

@Serializable
data class TaskResponseDto(
    val taskId: String,
    val title: String,
    val description: String,
    val priority: Priority,
    val status: Status
)
