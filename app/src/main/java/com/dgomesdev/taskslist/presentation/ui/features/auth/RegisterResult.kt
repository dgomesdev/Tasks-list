package com.dgomesdev.taskslist.presentation.ui.features.auth

import com.dgomesdev.taskslist.domain.model.User

sealed interface RegisterResult {
    data class Success(val user: User): RegisterResult
    data class Cancelled(val cancelledMessage: String): RegisterResult
    data class Failure(val failureMessage: String): RegisterResult
}