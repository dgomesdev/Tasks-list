package com.dgomesdev.taskslist.data.dto.request

import com.dgomesdev.taskslist.domain.model.Priority
import com.dgomesdev.taskslist.domain.model.Status
import com.dgomesdev.taskslist.domain.model.Task

data class TaskRequestDto(val task: Task) {
    val title: String = task.title
    val description: String = task.description
    val priority: Priority = task.priority
    val status: Status = task.status
}