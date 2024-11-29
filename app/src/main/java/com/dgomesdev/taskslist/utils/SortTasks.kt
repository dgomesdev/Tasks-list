package com.dgomesdev.taskslist.utils

import com.dgomesdev.taskslist.domain.model.Task

enum class SortOption {
    BY_TITLE,
    BY_PRIORITY,
    BY_STATUS
}

fun List<Task>.sortTasks(sortOption: SortOption): List<Task> {
    return when (sortOption) {
        SortOption.BY_TITLE -> this.sortedWith(
            compareBy<Task> { it.title }
                .thenByDescending { it.priority }
                .thenBy { it.status.ordinal }
        )
        SortOption.BY_PRIORITY -> this.sortedWith(
            compareByDescending<Task> { it.priority }
                .thenBy { it.title }
                .thenBy { it.status.ordinal }
        )
        SortOption.BY_STATUS -> this.sortedWith(
            compareBy<Task> { it.status.ordinal }
                .thenByDescending { it.priority }
                .thenBy { it.title }
        )
    }
}
