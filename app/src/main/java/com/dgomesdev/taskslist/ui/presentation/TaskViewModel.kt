package com.dgomesdev.taskslist.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dgomesdev.taskslist.TasksApplication
import com.dgomesdev.taskslist.data.repository.TaskRepositoryImpl
import com.dgomesdev.taskslist.domain.Status
import com.dgomesdev.taskslist.domain.TaskEntity
import com.dgomesdev.taskslist.utils.toDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(
    private val taskRepository: TaskRepositoryImpl,
) : ViewModel() {

    private val _uiState = MutableStateFlow(emptyList<TaskEntity>())
    val uiState: StateFlow<List<TaskEntity>> = _uiState

    init {
        getTaskList()
    }

    private fun getTaskList() = viewModelScope.launch {
        taskRepository.getTasks()
            .catch {}
            .collect { tasks ->
                tasks.verifyTaskStatus()
                _uiState.value = tasks
            }
    }

    private fun List<TaskEntity>.verifyTaskStatus() {
        val today = LocalDate.now()
        for (task in this) {
            if (task.isTaskDone) editTask(task.copy(status = Status.DONE))
            else {
                val deadline = task.endDate?.toDate()
                val dayBeforeDeadline = deadline?.minusDays(1)
                when {
                    deadline == null -> editTask(task.copy(status = Status.TO_DO))
                    today.isEqual(deadline) || today.isEqual(dayBeforeDeadline) ->
                        editTask(task.copy(status = Status.ALMOST_LATE))

                    today.isAfter(deadline) -> editTask(task.copy(status = Status.LATE))
                    else -> editTask(task.copy(status = Status.TO_DO))
                }
            }
        }
    }

    fun addTask(task: TaskEntity) = viewModelScope.launch {
        taskRepository.saveTask(task)
    }

    fun editTask(task: TaskEntity) = viewModelScope.launch {
        taskRepository.updateTask(task)
    }

    fun deleteTask(task: TaskEntity) = viewModelScope.launch {
        taskRepository.deleteTask(task)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val taskRepository = (this[APPLICATION_KEY] as TasksApplication).tasksRepository
                TaskViewModel(taskRepository = taskRepository)
            }
        }
    }
}