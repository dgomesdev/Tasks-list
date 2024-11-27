package com.dgomesdev.taskslist.domain.usecase.user

import android.content.Context
import android.util.Log
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.data.dto.request.AuthRequestDto
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.model.UserAction
import com.dgomesdev.taskslist.domain.repository.UserRepository
import com.dgomesdev.taskslist.infra.SecurePreferences
import kotlinx.coroutines.flow.single

class AuthUseCase(
    private val userRepository: UserRepository,
    private val securePreferences: SecurePreferences,
    private val context: Context
) {
    suspend operator fun invoke(userAction: UserAction, user: User): Pair<User?, String> {
        Log.w("AuthUseCase", "User: $user")
        Log.w("AuthUseCase", "UserAction: $userAction")
        val response = when (userAction) {
            UserAction.REGISTER -> userRepository.saveUser(AuthRequestDto.create(user)).single()
            UserAction.LOGIN -> userRepository.loginUser(AuthRequestDto.create(user)).single()
            else -> throw Exception("Invalid user action")
        }
        securePreferences.saveToken(response.token)
        Log.d("AuthUseCase", "Response: $response")
        val loggedUser = User.fromApi(response.user)
        return Pair(loggedUser, context.getString(R.string.welcome, loggedUser.username))
    }
}