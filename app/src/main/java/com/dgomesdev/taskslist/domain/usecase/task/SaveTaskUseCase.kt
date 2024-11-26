package com.dgomesdev.taskslist.domain.usecase.task

import com.dgomesdev.taskslist.data.dto.request.TaskRequestDto
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.TaskRepository
import com.dgomesdev.taskslist.domain.repository.UserRepository
import kotlinx.coroutines.flow.single

class SaveTaskUseCase(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(task: Task, userId: String): Pair<User?, String> {
        Task.fromApi(taskRepository.saveTask(TaskRequestDto.create(task)).single())
        return Pair(User.fromApi(userRepository.getUser(userId).single()), "Task saved successfully")
    }
}