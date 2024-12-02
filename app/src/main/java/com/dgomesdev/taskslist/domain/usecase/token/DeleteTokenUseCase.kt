package com.dgomesdev.taskslist.domain.usecase.token

import com.dgomesdev.taskslist.infra.SecurePreferences

class DeleteTokenUseCase(private val securePreferences: SecurePreferences) {
    operator fun invoke() {
        securePreferences.deleteToken()
    }
}