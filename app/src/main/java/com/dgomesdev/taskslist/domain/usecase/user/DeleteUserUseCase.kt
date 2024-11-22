package com.dgomesdev.taskslist.domain.usecase.user

import com.dgomesdev.taskslist.domain.repository.UserRepository
import java.util.UUID

class DeleteUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: UUID): String =
        userRepository.deleteUser(userId).message
}