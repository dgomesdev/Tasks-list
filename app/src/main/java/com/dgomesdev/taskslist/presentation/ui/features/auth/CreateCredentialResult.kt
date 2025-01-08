package com.dgomesdev.taskslist.presentation.ui.features.auth

import com.dgomesdev.taskslist.domain.model.User

sealed interface CreateCredentialResult {
    data class Success(val user: User): CreateCredentialResult
    data class Cancelled(val user: User, val cancelledMessage: String): CreateCredentialResult
    data class Failure(val user: User, val failureMessage: String): CreateCredentialResult
}