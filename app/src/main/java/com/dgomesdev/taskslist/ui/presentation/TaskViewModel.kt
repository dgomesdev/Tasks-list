package com.dgomesdev.taskslist.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgomesdev.taskslist.data.TaskRepositoryImpl
import com.dgomesdev.taskslist.domain.TaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
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

}

data class TaskUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val loading: Boolean = false
)