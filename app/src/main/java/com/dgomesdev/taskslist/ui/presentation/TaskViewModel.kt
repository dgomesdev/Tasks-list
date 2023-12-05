package com.dgomesdev.taskslist.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dgomesdev.taskslist.TasksApplication
import com.dgomesdev.taskslist.data.repository.TaskRepositoryImpl
import com.dgomesdev.taskslist.domain.TaskEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskRepository: TaskRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState(loading = true))
    val uiState: StateFlow<TaskUiState> = _uiState

    init {
        getTaskList()
    }

    fun getTaskList() = viewModelScope.launch {
        taskRepository.getTasks()
            .catch {}
            .collect { tasks ->
                _uiState.value = TaskUiState(tasks = tasks)
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

data class TaskUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val loading: Boolean = false
)