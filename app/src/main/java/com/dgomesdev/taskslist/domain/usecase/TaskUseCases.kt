package com.dgomesdev.taskslist.domain.usecase

import com.dgomesdev.taskslist.domain.usecase.task.DeleteTaskUseCase
import com.dgomesdev.taskslist.domain.usecase.task.SaveTaskUseCase
import com.dgomesdev.taskslist.domain.usecase.task.UpdateTaskUseCase

data class TaskUseCases(
    val saveTaskUseCase: SaveTaskUseCase,
    val updateTaskUseCase: UpdateTaskUseCase,
    val deleteTaskUseCase: DeleteTaskUseCase
)