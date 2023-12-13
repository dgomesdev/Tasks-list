package com.dgomesdev.taskslist.utils

fun validateTaskName(taskName: String): Boolean {
    return taskName.isNotBlank()
}