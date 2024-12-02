package com.dgomesdev.taskslist.domain.usecase.task

import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.TaskRepository
import com.dgomesdev.taskslist.domain.repository.UserRepository
import kotlinx.coroutines.flow.single

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(taskId: String, userId: String): Pair<User?, String> {
        val message = taskRepository.deleteTask(taskId).single().message.toString()
        val response = userRepository.getUser(userId).single()
        return Pair(User.fromApi(response), message)
    }
}