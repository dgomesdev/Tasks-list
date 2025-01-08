package com.dgomesdev.taskslist.domain.usecase.task

import android.content.Context
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.data.dto.request.TaskRequestDto
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.single

class SaveTaskUseCase(private val taskRepository: TaskRepository, private val context: Context) {
    suspend operator fun invoke(task: Task): Pair<User?, String> {
        val response = User.fromApi(taskRepository.saveTask(TaskRequestDto.create(task)).single())
        return Pair(
            response,
            context.getString(R.string.task_saved)
        )
    }
}