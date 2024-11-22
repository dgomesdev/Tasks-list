package com.dgomesdev.taskslist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.model.TaskAction
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.model.UserAction
import com.dgomesdev.taskslist.domain.usecase.task.DeleteTaskUseCase
import com.dgomesdev.taskslist.domain.usecase.task.GetTaskUseCase
import com.dgomesdev.taskslist.domain.usecase.task.SaveTaskUseCase
import com.dgomesdev.taskslist.domain.usecase.task.UpdateTaskUseCase
import com.dgomesdev.taskslist.domain.usecase.user.AuthUseCase
import com.dgomesdev.taskslist.domain.usecase.user.DeleteUserUseCase
import com.dgomesdev.taskslist.domain.usecase.user.GetUserUseCase
import com.dgomesdev.taskslist.domain.usecase.user.UpdateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TasksViewModel(
    private val saveTaskUseCase: SaveTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val authUseCase: AuthUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onTaskChange = { taskAction, task ->
                    handleTaskAction(taskAction, task)
                },
                onUserChange = { userAction, user ->
                    handleUserAction(userAction, user)
                }
            )
        }
    }

    private fun handleTaskAction(action: TaskAction, task: Task) {
        when (action) {
            TaskAction.SAVE -> executeTask { saveTaskUseCase(task) }
            TaskAction.GET -> executeTask { getTaskUseCase(task.taskId!!, _uiState.value.user!!) }
            TaskAction.UPDATE -> executeTask { updateTaskUseCase(task) }
            TaskAction.DELETE -> executeTask { deleteTaskUseCase(task.taskId!!) }
        }
    }

    private fun handleUserAction(action: UserAction, user: User) {
        when (action) {
            UserAction.REGISTER, UserAction.LOGIN -> executeUser { authUseCase(action, user) }
            UserAction.GET -> executeUser { getUserUseCase(user.userId!!) }
            UserAction.UPDATE -> executeUser { updateUserUseCase(user) }
            UserAction.DELETE -> executeUser { deleteUserUseCase(user.userId!!) }
        }
    }

    private fun <T> executeTask(taskAction: suspend () -> T) {
        viewModelScope.launch {
            runCatching {
                _uiState.value = _uiState.value.copy(message = null)
                taskAction()
            }.onSuccess { result ->
                _uiState.value = _uiState.value.copy(task = result as? Task, message = "Success!")
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(message = "Error: ${e.message}")
            }
        }
    }

    private fun <T> executeUser(userAction: suspend () -> T) {
        viewModelScope.launch {
            runCatching {
                _uiState.value = _uiState.value.copy(message = null)
                userAction()
            }.onSuccess { result ->
                _uiState.value = _uiState.value.copy(user = result as? User, message = "Success!")
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(message = "Error: ${e.message}")
            }
        }
    }
}

data class AppUiState(
    val task: Task? = null,
    val user: User? = null,
    val onTaskChange: (TaskAction, Task) -> Unit = {_ , _ ->},
    val onUserChange: (UserAction, User) -> Unit = {_ , _ ->},
    val message: String? = null
)