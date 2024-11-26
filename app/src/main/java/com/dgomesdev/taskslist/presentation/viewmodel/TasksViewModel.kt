package com.dgomesdev.taskslist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.model.TaskAction
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.model.UserAction
import com.dgomesdev.taskslist.domain.usecase.task.DeleteTaskUseCase
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
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val authUseCase: AuthUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    securePreferences: SecurePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        val loggedUser = securePreferences.getUserFromToken()
        if (loggedUser != null) handleUserAction(UserAction.GET, loggedUser)
        _uiState.update { currentState ->
            currentState.copy(
                onTaskChange = ::handleTaskAction,
                onUserChange = ::handleUserAction,
                onLogout = ::logout,
                onRefreshMessage = ::refreshMessage
            )
        }
    }

    private fun handleTaskAction(action: TaskAction, task: Task) {
        Log.d("TASKVIEWMODEL", "Task: $task, Action: $action")
        val userId = _uiState.value.user?.userId ?: error("No user logged in")
        when (action) {
            TaskAction.SAVE -> execute { saveTaskUseCase(task, userId) }
            TaskAction.UPDATE -> execute { updateTaskUseCase(task, userId) }
            TaskAction.DELETE -> execute { deleteTaskUseCase(task.taskId!!, userId) }
        }
    }

    private fun handleUserAction(action: UserAction, user: User) {
        Log.d("TASKVIEWMODEL", "User: $user, Action: $action")
        when (action) {
            UserAction.REGISTER, UserAction.LOGIN -> execute { authUseCase(action, user) }
            UserAction.GET -> execute { getUserUseCase(user.userId!!) }
            UserAction.UPDATE -> execute { updateUserUseCase(user) }
            UserAction.DELETE -> execute { deleteUserUseCase(user.userId!!) }
        }
    }

    private fun execute(action: suspend () -> Pair<User?, String>) {
        viewModelScope.launch {
            runCatching {
                _uiState.update {
                    it.copy(
                        message = null,
                        isLoading = true
                    )
                }
                action()
            }.onSuccess { result ->
                val (user, message) = result
                _uiState.update {
                    it.copy(
                        user = user,
                        message = message,
                        isLoading = false
                    )
                }
                Log.d("TasksViewModel", "User: ${_uiState.value.user}")
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        message = "Error: ${e.message}",
                        isLoading = false
                    )
                }
                Log.e("TasksViewModel", "Error: ${e.message}")
            }
        }
    }

    private fun logout() {
        _uiState.update {
            it.copy(user = null)
        }
    }

    private fun refreshMessage() {
        _uiState.update {
            it.copy(message = null)
        }
    }
}

data class AppUiState(
    val user: User? = null,
    val onTaskChange: (TaskAction, Task) -> Unit = { _, _ -> },
    val onUserChange: (UserAction, User) -> Unit = { _, _ -> },
    val onLogout: () -> Unit = {},
    val onRefreshMessage: () -> Unit = {},
    val message: String? = null,
    val isLoading: Boolean = false
)