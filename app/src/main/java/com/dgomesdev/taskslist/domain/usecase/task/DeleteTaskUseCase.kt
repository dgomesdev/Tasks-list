package com.dgomesdev.taskslist.domain.usecase.task

import android.content.Context
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.single

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository, private val context: Context
) {
    suspend operator fun invoke(taskId: String): Pair<User?, String?> {
        val response = User.fromApi(taskRepository.deleteTask(taskId).single())
        return Pair(
            response,
            context.getString(R.string.task_deleted)
        )
    }
}