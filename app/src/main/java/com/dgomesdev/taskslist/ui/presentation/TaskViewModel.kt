package com.dgomesdev.taskslist.ui.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import java.time.LocalDateTime

class TaskViewModel(
    private val taskRepository: TaskRepositoryImpl,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState(loading = true))
    val uiState: StateFlow<TaskUiState> = _uiState

    private val _dateValidationState = mutableStateOf(true)
    val dateValidationState: MutableState<Boolean> = _dateValidationState

    init {
        getTaskList()
    }

    fun getTaskList() = viewModelScope.launch {
        taskRepository.getTasks()
            .catch {}
            .collect { tasks ->
                _uiState.value = TaskUiState(tasks = tasks.verifyTaskStatus())
            }
    }

    private fun List<TaskEntity>.verifyTaskStatus(): List<TaskEntity> {
        val verifiedTasks = mutableListOf<TaskEntity>()
        if (this == emptyList<TaskEntity>()) return emptyList()
        for (task in this) {
            when {
                task.endDate == null -> verifiedTasks.add(task.copy(status = Status.TO_DO))
                task.endDate.toDate()
                    .isBefore(LocalDate.now().minusDays(1)) -> verifiedTasks.add(
                    task.copy(
                        status = Status.ALMOST_LATE
                    )
                )

                task.endDate.toDate().isAfter(LocalDate.now()) -> verifiedTasks.add(
                    task.copy(
                        status = Status.LATE
                    )
                )

                else -> verifiedTasks.add(task.copy(status = Status.TO_DO))
            }
        }
        return verifiedTasks
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
    fun validateEndDate(startDate: String?, endDate: String?) {
        if (startDate != null && endDate != null)
            _dateValidationState.value = endDate.toDate().isAfter(startDate.toDate())
        else _dateValidationState.value = true
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

data class TaskUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val loading: Boolean = false,
)