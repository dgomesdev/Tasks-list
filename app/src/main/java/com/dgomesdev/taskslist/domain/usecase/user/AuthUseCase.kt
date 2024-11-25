package com.dgomesdev.taskslist.domain.usecase.user

import android.util.Log
import com.dgomesdev.taskslist.data.dto.request.AuthRequestDto
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.model.UserAction
import com.dgomesdev.taskslist.domain.repository.UserRepository
import com.dgomesdev.taskslist.infra.SecurePreferences
import kotlinx.coroutines.flow.single

class AuthUseCase(
    private val userRepository: UserRepository,
    private val securePreferences: SecurePreferences
) {
    suspend operator fun invoke(userAction: UserAction, user: User): User {
        Log.w("AuthUseCase", "User: $user")
        Log.w("AuthUseCase", "UserAction: $userAction")
        val response = when (userAction) {
            UserAction.REGISTER -> userRepository.saveUser(AuthRequestDto.create(user)).single()
            UserAction.LOGIN -> userRepository.loginUser(AuthRequestDto.create(user)).single()
            else -> throw Exception("Invalid user action")
        }
        securePreferences.saveToken(response.token)
        Log.d("AuthUseCase", "Response: $response")
        return User.fromApi(response.user)
    }
}