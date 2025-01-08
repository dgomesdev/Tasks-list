package com.dgomesdev.taskslist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.usecase.TaskUseCases
import com.dgomesdev.taskslist.domain.usecase.UserUseCases
import com.dgomesdev.taskslist.infra.SecurePreferences
import com.dgomesdev.taskslist.presentation.ui.features.auth.CreateCredentialResult.Cancelled
import com.dgomesdev.taskslist.presentation.ui.features.auth.CreateCredentialResult.Failure
import com.dgomesdev.taskslist.presentation.ui.features.auth.CreateCredentialResult.Success
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.DeleteTask
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.DeleteUser
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.GetUser
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.Login
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.Logout
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.RecoverPassword
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.RefreshMessage
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.Register
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.ResetPassword
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.SaveTask
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.SetRecoveryCode
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.ShowSnackbar
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.UpdateTask
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.UpdateUser
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

    init {
        initializeUser()
    }

    private fun initializeUser() {
        viewModelScope.launch {
            val tokenUser = securePreferences.getUserFromToken()
            if (tokenUser != null){
                val result = runCatching { userUseCases.getUserUseCase(tokenUser.userId) }
                result.onSuccess { (user, message) ->
                    setState(user = user, message = message, isSessionValid = true)
                }.onFailure { e ->
                    setState(
                        message = "Failed to load user: ${e.message}",
                        isSessionValid = false
                    )
                }
            } else setState(isSessionValid = false)
        }
    }

    private fun setState(
        user: User? = null,
        message: String? = null,
        isLoading: Boolean = false,
        recoveryCode: String? = null,
        isSessionValid: Boolean = true
    ) {
        _uiState.update {
            it.copy(
                user = user ?: it.user,
                message = message ?: it.message,
                isLoading = isLoading,
                recoveryCode = recoveryCode ?: it.recoveryCode,
                isSessionValid = isSessionValid
            )
        }
    }

    fun handleAppUiIntent(intent: AppUiIntent) {
        Log.d("ViewModel", "Intent: $intent")
        when (intent) {
            is Register -> {
                when (intent.result) {
                    is Success -> {
                        _uiState.update { it.copy(hasGoogleCredential = true) }
                        execute { userUseCases.registerUseCase(intent.result.user) }
                    }
                    is Cancelled -> {
                        execute { userUseCases.registerUseCase(intent.result.user) }
                    }
                    is Failure -> {
                        execute { userUseCases.registerUseCase(intent.result.user) }
                    }
                }
            }

            is Login -> execute { userUseCases.loginUseCase(intent.user) }
            is GetUser -> execute { userUseCases.getUserUseCase(intent.user.userId) }
            is UpdateUser -> execute { userUseCases.updateUserUseCase(intent.user) }
            is DeleteUser -> execute { userUseCases.deleteUserUseCase(intent.user.userId) }
            is RecoverPassword -> execute { userUseCases.recoverPasswordUseCase(intent.user) }
            is ResetPassword -> execute {
                userUseCases.resetPasswordUseCase(
                    intent.recoveryCode,
                    intent.user
                )
            }

            is SaveTask -> execute { taskUseCases.saveTaskUseCase(intent.task) }
            is UpdateTask -> execute { taskUseCases.updateTaskUseCase(intent.task) }
            is DeleteTask -> execute { taskUseCases.deleteTaskUseCase(intent.task.taskId) }
            is SetRecoveryCode -> setState(recoveryCode = intent.recoveryCode)
            is ShowSnackbar -> showSnackbar(intent.message)
            is RefreshMessage -> _uiState.update { it.copy(message = null) }
            is Logout -> logout()
        }
    }

    private fun execute(useCase: suspend () -> Pair<User?, String?>) {
        viewModelScope.launch {
            runCatching {
                setState(message = null, isLoading = true)
                useCase()
            }.onSuccess { result ->
                val (user, message) = result
                if (user != null) setState(user = user, message = message)
                else logout()
            }.onFailure { e ->
                val message = e.message.toString()
                setState(message = "Error: $message")
                if (message.contains("401") || message.contains("403")) logout()
            }
        }
    }

    private fun showSnackbar(message: String) {
        viewModelScope.launch {
            uiState.value.snackbarHostState.showSnackbar(message)
        }
    }

    private fun logout() {
        securePreferences.deleteToken()
        _uiState.update { it.copy(user = null, isSessionValid = false, message = "logout") }
    }

}