package com.dgomesdev.taskslist.presentation.viewmodel

import androidx.compose.material3.SnackbarHostState
import com.dgomesdev.taskslist.domain.model.User

data class AppUiState(
    val user: User? = null,
    val message: String? = null,
    val isLoading: Boolean = false,
    val recoveryCode: String? = null,
    val snackbarHostState: SnackbarHostState = SnackbarHostState(),
    val isSessionValid: Boolean? = null
)