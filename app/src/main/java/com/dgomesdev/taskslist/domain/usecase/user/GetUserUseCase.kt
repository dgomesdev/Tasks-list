package com.dgomesdev.taskslist.domain.usecase.user

import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.UserRepository
import java.util.UUID

class GetUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: UUID): User =
        User.fromApi(userRepository.getUser(userId))
}