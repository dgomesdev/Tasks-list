package com.dgomesdev.taskslist.domain.usecase.user

import android.content.Context
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.UserRepository
import com.dgomesdev.taskslist.infra.SecurePreferences
import kotlinx.coroutines.flow.single

class RegisterUseCase(
    private val userRepository: UserRepository,
    private val securePreferences: SecurePreferences,
    private val context: Context
) {
    suspend operator fun invoke(user: User): Pair<User?, String?> {
        val newUser = userRepository.registerUser(UserRequestDto.register(user)).single()
        newUser.token?.let { securePreferences.saveToken(it) }
        val loggedUser = User.fromApi(newUser)
        return Pair(loggedUser, context.getString(R.string.welcome, loggedUser.username))
    }
}