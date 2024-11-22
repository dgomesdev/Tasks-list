package com.dgomesdev.taskslist.data.dto.response

import com.dgomesdev.taskslist.domain.model.Priority
import com.dgomesdev.taskslist.domain.model.Status
import java.util.UUID

data class TaskResponseDto(
    val taskId: UUID,
    val title: String,
    val description: String,
    val priority: Priority,
    val status: Status
)
