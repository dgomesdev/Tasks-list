package com.dgomesdev.taskslist.domain.usecase.task

import com.dgomesdev.taskslist.domain.model.Task

class GetTaskUseCase {
    operator fun invoke(task: Task): Task = task
}
