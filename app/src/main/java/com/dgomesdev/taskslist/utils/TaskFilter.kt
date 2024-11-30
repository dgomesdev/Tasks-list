package com.dgomesdev.taskslist.utils

import com.dgomesdev.taskslist.domain.model.Priority
import com.dgomesdev.taskslist.domain.model.Status
import com.dgomesdev.taskslist.domain.model.Task

data class TaskFilter(
    val priorities: Set<Priority> = emptySet(),
    val statuses: Set<Status> = emptySet()
)

fun List<Task>.filterTasks(filter: TaskFilter): List<Task> {
    return this.filter { task ->
        (filter.priorities.isEmpty() || filter.priorities.contains(task.priority)) &&
                (filter.statuses.isEmpty() || filter.statuses.contains(task.status))
    }
}
