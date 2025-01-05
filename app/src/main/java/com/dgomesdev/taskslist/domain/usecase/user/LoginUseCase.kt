package com.dgomesdev.taskslist.domain.usecase.user

import android.content.Context
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.UserRepository
import com.dgomesdev.taskslist.infra.SecurePreferences
import kotlinx.coroutines.flow.single

class LoginUseCase(
    private val userRepository: UserRepository,
    private val securePreferences: SecurePreferences,
    private val context: Context
) {
    suspend operator fun invoke(user: User): Pair<User?, String?> {
        val loginUser = userRepository.loginUser(UserRequestDto.login(user)).single()
        loginUser.token?.let { securePreferences.saveToken(it) }
        val loggedUser = User.fromApi(loginUser)
        return Pair(loggedUser, context.getString(R.string.welcome, loggedUser.username))
    }
}