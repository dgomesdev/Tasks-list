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
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.*
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
                }.onFailure { logout() }
            } else setState(isSessionValid = false)
        }
    }

    private fun setState(
        user: User? = uiState.value.user,
        email: String = uiState.value.email,
        password: String = uiState.value.password,
        message: String? = uiState.value.message,
        isLoading: Boolean = uiState.value.isLoading,
        recoveryCode: String? = uiState.value.recoveryCode,
        isSessionValid: Boolean = uiState.value.isSessionValid,
        hasGoogleCredential: Boolean = uiState.value.hasGoogleCredential
    ) {
        _uiState.update {
            it.copy(
                user = user,
                email = email,
                password = password,
                message = message,
                isLoading = isLoading,
                recoveryCode = recoveryCode,
                isSessionValid = isSessionValid,
                hasGoogleCredential = hasGoogleCredential
            )
        }
    }

    fun handleAppUiIntent(intent: AppUiIntent) {
        Log.d("ViewModel", "Intent: $intent")
        when (intent) {
            is Register -> {
                when (val result = intent.result) {
                    is Success -> {
                        setState(hasGoogleCredential = true)
                        execute { userUseCases.registerUseCase(result.user) }
                    }
                    is Cancelled -> {
                        setState(hasGoogleCredential = false)
                        execute { userUseCases.registerUseCase(result.user) }
                    }
                    is Failure -> {
                        setState(hasGoogleCredential = false)
                        execute { userUseCases.registerUseCase(result.user) }
                    }
                }
            }
            is Login -> execute {
                setState(isSessionValid = true)
                userUseCases.loginUseCase(intent.user)
            }
            is GetUser -> execute { userUseCases.getUserUseCase(intent.user.userId) }
            is UpdateUser -> execute { userUseCases.updateUserUseCase(intent.user) }
            is DeleteUser -> execute { userUseCases.deleteUserUseCase(intent.user.userId) }
            is RecoverPassword -> execute { userUseCases.recoverPasswordUseCase(intent.user) }
            is ResetPassword -> {
                execute { userUseCases.resetPasswordUseCase(intent.recoveryCode, intent.user) }
                setState(recoveryCode = null)
            }
            is SaveTask -> execute { taskUseCases.saveTaskUseCase(intent.task) }
            is UpdateTask -> execute { taskUseCases.updateTaskUseCase(intent.task) }
            is DeleteTask -> execute { taskUseCases.deleteTaskUseCase(intent.task.taskId) }
            is SetRecoveryCode -> setState(recoveryCode = intent.recoveryCode)
            is ShowSnackbar -> showSnackbar(intent.message)
            is SetEmail -> setState(email = intent.email)
            is SetPassword -> setState(password = intent.password)
            is SetHasGoogleCredential -> setState(hasGoogleCredential = intent.hasGoogleCredential)
            is SetIsSessionValid -> setState(isSessionValid = intent.isSessionValid)
            is Logout -> logout()
        }
    }

    private fun execute(useCase: suspend () -> Pair<User?, String?>) {
        viewModelScope.launch {
            runCatching {
                setState(isLoading = true)
                useCase()
            }.onSuccess { result ->
                val (user, message) = result
                if (user == null) logout()
                else setState(user = user, message = message, isLoading = false, isSessionValid = true)
            }.onFailure { e ->
                val message = e.message.toString().replace("java.lang.Exception: ", "")
                setState(message = "Error: $message", isLoading = false)
                if (message.contains("401") || message.contains("403")) {
                    setState(message = null)
                    logout()
                }
            }
        }
    }

    private fun showSnackbar(message: String) {
        viewModelScope.launch {
            uiState.value.snackbarHostState.showSnackbar(message)
            setState(message = null)
        }
    }

    private fun logout() {
        securePreferences.deleteToken()
        setState(user = null, isSessionValid = false, isLoading = false)
    }

}