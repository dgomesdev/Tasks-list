package com.dgomesdev.taskslist.presentation.viewmodel

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.usecase.TaskUseCases
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.usecase.UserUseCases
import com.dgomesdev.taskslist.infra.SecurePreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TasksViewModel(
    private val userUseCases: UserUseCases,
    private val taskUseCases: TaskUseCases,
    private val securePreferences: SecurePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    val snackbarHostState = SnackbarHostState()
    fun showSnackbar(message: String) {
        viewModelScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    init {
        securePreferences.getUserFromToken()?.let { handleAppUiIntent(AppUiIntent.GetUser(it)) }
    }

    fun handleAppUiIntent(intent: AppUiIntent) {
        val user = intent.user
        val task = intent.task
        Log.d("ViewModel", "Intent: $intent")
        when (intent) {
            is AppUiIntent.Register -> execute { userUseCases.registerUseCase(
                user!!
            ) }
            is AppUiIntent.Login -> execute { userUseCases.loginUseCase(
                user!!
            ) }
            is AppUiIntent.GetUser -> execute { userUseCases.getUserUseCase(
                user!!.userId
            ) }
            is AppUiIntent.UpdateUser -> execute { userUseCases.updateUserUseCase(
                user!!
            ) }
            is AppUiIntent.DeleteUser -> execute { userUseCases.deleteUserUseCase(
                user!!.userId
            ) }
            is AppUiIntent.RecoverPassword -> execute { userUseCases.recoverPasswordUseCase(
                user!!
            ) }
            is AppUiIntent.ResetPassword -> execute { userUseCases.resetPasswordUseCase(
                intent.recoveryCode!!,
                user!!
            ) }
            is AppUiIntent.SaveTask -> execute { taskUseCases.saveTaskUseCase(
                task!!,
                user!!.userId
            ) }
            is AppUiIntent.UpdateTask -> execute { taskUseCases.updateTaskUseCase(
                task!!,
                user!!.userId
            ) }
            is AppUiIntent.DeleteTask -> execute { taskUseCases.deleteTaskUseCase(
                task!!.taskId,
                user!!.userId
            ) }
            is AppUiIntent.Logout -> logout()
            is AppUiIntent.RefreshMessage -> refreshMessage()
            is AppUiIntent.SetRecoveryCode -> setRecoveryCode(intent.recoveryCode!!)
        }
    }

    private fun execute(useCase: suspend () -> Pair<User?, String?>) {
        viewModelScope.launch {
            runCatching {
                _uiState.update {
                    it.copy(
                        message = null,
                        isLoading = true
                    )
                }
                useCase()
            }.onSuccess { result ->
                val (user, message) = result
                _uiState.update {
                    it.copy(
                        user = user,
                        message = message,
                        isLoading = false,
                    )
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        message = "Error: ${e.message}",
                        isLoading = false
                    )
                }
                if (e.message.toString().contains("401") || e.message.toString().contains("403")) logout()
            }
        }
    }

    private fun logout() {
        securePreferences.deleteToken()
        _uiState.update {
            it.copy(user = null)
        }
    }

    private fun refreshMessage() {
        _uiState.update {
            it.copy(message = null)
        }
    }

    private fun setRecoveryCode(recoveryCode: String) {
        _uiState.update {
            it.copy(recoveryCode = recoveryCode)
        }
    }
}

data class AppUiState(
    val user: User? = null,
    val message: String? = null,
    val isLoading: Boolean = false,
    val recoveryCode: String? = null
)

sealed class AppUiIntent(
    val user: User? = null,
    val recoveryCode: String? = null,
    val task: Task? = null
) {
    class Register(user: User): AppUiIntent(user)
    class Login(user: User): AppUiIntent(user)
    class GetUser(user: User): AppUiIntent(user)
    class UpdateUser(user: User): AppUiIntent(user)
    class DeleteUser(user: User): AppUiIntent(user)
    class RecoverPassword(user: User): AppUiIntent(user)
    class ResetPassword(recoveryCode: String, user: User): AppUiIntent(user, recoveryCode)
    class SaveTask(task: Task): AppUiIntent(task = task)
    class UpdateTask(task: Task): AppUiIntent(task = task)
    class DeleteTask(task: Task): AppUiIntent(task = task)
    class Logout: AppUiIntent()
    class RefreshMessage: AppUiIntent()
    class SetRecoveryCode(recoveryCode: String): AppUiIntent(recoveryCode = recoveryCode)
}