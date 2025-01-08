package com.dgomesdev.taskslist.presentation.ui.features.auth

import com.dgomesdev.taskslist.domain.model.User

sealed interface LoginResult {
    data class Success(val user: User): LoginResult
    data class Cancelled(val cancelledMessage: String): LoginResult
    data class Failure(val failureMessage: String): LoginResult
    data class NoCredentials(val noCredentialsMessage: String): LoginResult
}