package com.dgomesdev.taskslist.domain.usecase.user

import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.UserRepository
import kotlinx.coroutines.flow.single

class GetUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String): User =
        User.fromApi(userRepository.getUser(userId).single())
}