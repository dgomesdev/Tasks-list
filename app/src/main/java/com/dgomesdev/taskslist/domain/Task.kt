package com.dgomesdev.taskslist.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

typealias TaskEntity = Task
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    )
