package com.dgomesdev.taskslist.domain.usecase.task

import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.model.User
import java.util.UUID

class GetTaskUseCase {
    operator fun invoke(taskId: UUID, user: User): Task =
        user.tasks.first { task -> task.taskId == taskId }
}
