package com.dgomesdev.taskslist.domain.usecase.token

import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.infra.SecurePreferences

class GetUserFromTokenUseCase(private val securePreferences: SecurePreferences) {
    operator fun invoke(): User? = securePreferences.getUserFromToken()
}