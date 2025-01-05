package com.dgomesdev.taskslist.domain.usecase.user

import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.UserRepository
import kotlinx.coroutines.flow.single

class RecoverPasswordUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User): Pair<User?, String?> {
        val response = userRepository.recoverPassword(UserRequestDto.recoverPassword(user)).single().message
        return Pair(null, response)
    }
}