package com.dgomesdev.taskslist.domain.usecase.user

import com.dgomesdev.taskslist.data.dto.request.AuthRequestDto
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.model.UserAction
import com.dgomesdev.taskslist.domain.repository.UserRepository
import com.dgomesdev.taskslist.infra.SecurePreferences

class AuthUseCase(
    private val userRepository: UserRepository,
    private val securePreferences: SecurePreferences
) {
    suspend operator fun invoke(userAction: UserAction, user: User): User {
        val response = when (userAction) {
            UserAction.REGISTER -> userRepository.saveUser(AuthRequestDto(user))
            UserAction.LOGIN -> userRepository.loginUser(AuthRequestDto(user))
            else -> throw Exception("Invalid user action")
        }
        securePreferences.saveToken(response.token)
        return User.fromApi(response.user)
    }
}