package com.dgomesdev.taskslist.presentation.ui.features.auth

import com.dgomesdev.taskslist.domain.model.User

sealed interface GetCredentialResult {
    data class Success(val user: User): GetCredentialResult
    data class Cancelled(val cancelledMessage: String): GetCredentialResult
    data class Failure(val failureMessage: String): GetCredentialResult
    data class NoCredentials(val noCredentialsMessage: String): GetCredentialResult
}