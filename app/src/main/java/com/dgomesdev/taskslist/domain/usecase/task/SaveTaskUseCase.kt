package com.dgomesdev.taskslist.domain.usecase.task

import com.dgomesdev.taskslist.data.dto.request.TaskRequestDto
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.single

class SaveTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task): Task =
        Task.fromApi(taskRepository.saveTask(TaskRequestDto.create(task)).single())

}