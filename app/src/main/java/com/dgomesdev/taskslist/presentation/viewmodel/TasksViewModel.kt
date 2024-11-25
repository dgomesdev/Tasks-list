package com.dgomesdev.taskslist.presentation.viewmodel

import android.util.Log
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
import com.dgomesdev.taskslist.infra.SecurePreferences
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
    private val deleteUserUseCase: DeleteUserUseCase,
    private val securePreferences: SecurePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        checkUserLoggedIn()
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

    private fun checkUserLoggedIn() {
        _uiState.update { currentState ->
            currentState.copy(isLoggedIn = securePreferences.isTokenValid())
        }
        if (_uiState.value.isLoggedIn) {
            handleUserAction(UserAction.GET, securePreferences.getUserFromToken()!!)
        }
    }

    private fun handleTaskAction(action: TaskAction, task: Task) {
        Log.d("TASKVIEWMODEL", "Task: $task")
        Log.d("TASKVIEWMODEL", "TaskAction: $action")
        when (action) {
            TaskAction.SAVE -> executeTask { saveTaskUseCase(task) }
            TaskAction.GET -> executeTask { getTaskUseCase(task) }
            TaskAction.UPDATE -> executeTask { updateTaskUseCase(task) }
            TaskAction.DELETE -> executeTask { deleteTaskUseCase(task.taskId!!) }
        }
    }

    private fun handleUserAction(action: UserAction, user: User) {
        Log.d("TASKVIEWMODEL", "User: $user")
        Log.d("TASKVIEWMODEL", "UserAction: $action")
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
                _uiState.value = _uiState.value.copy(
                    message = null,
                    isLoading = true
                )
                taskAction()
            }.onSuccess {
                handleUserAction(UserAction.GET, _uiState.value.user!!)
            }.onFailure { e ->
                _uiState.value =
                    _uiState.value.copy(
                        message = "Error: ${e.message}",
                        isLoading = false
                    )
                Log.e("TasksViewModel", "Error: ${e.message}")
            }
        }
    }

    private fun <T> executeUser(userAction: suspend () -> T) {
        viewModelScope.launch {
            runCatching {
                _uiState.value = _uiState.value.copy(
                    message = null,
                    isLoading = true
                )
                userAction()
            }.onSuccess { result ->
                if (result is User) {
                    _uiState.update {
                        it.copy(
                            user = result,
                            isLoggedIn = securePreferences.isTokenValid(),
                            message = "Success!",
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            message = "Account deleted successfully",
                            isLoading = false
                        )
                    }
                }
                Log.d("TasksViewModel", "User: ${_uiState.value.user}")
            }.onFailure { e ->
                _uiState.value =
                    _uiState.value.copy(
                        message = "Error: ${e.message}",
                        isLoading = false
                    )
                Log.e("TasksViewModel", "Error: ${e.message}")
            }
        }
    }
}

data class AppUiState(
    val user: User? = null,
    val onTaskChange: (TaskAction, Task) -> Unit = { _, _ -> },
    val onUserChange: (UserAction, User) -> Unit = { _, _ -> },
    val message: String? = null,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false
)