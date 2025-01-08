package com.dgomesdev.taskslist.presentation.viewmodel

import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.presentation.ui.features.auth.RegisterResult

sealed interface AppUiIntent {
    data class Register(val result: RegisterResult) : AppUiIntent
    data class Login(val user: User) : AppUiIntent
    data class GetUser(val user: User) : AppUiIntent
    data class UpdateUser(val user: User) : AppUiIntent
    data class DeleteUser(val user: User) : AppUiIntent
    data class RecoverPassword(val user: User) : AppUiIntent
    data class ResetPassword(val recoveryCode: String, val user: User) : AppUiIntent
    data class SaveTask(val task: Task) : AppUiIntent
    data class UpdateTask(val task: Task) : AppUiIntent
    data class DeleteTask(val task: Task) : AppUiIntent
    data class SetRecoveryCode(val recoveryCode: String) : AppUiIntent
    data class ShowSnackbar(val message: String): AppUiIntent
    data object Logout : AppUiIntent
    data object RefreshMessage : AppUiIntent
}