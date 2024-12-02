package com.dgomesdev.taskslist.domain.usecase.user

import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.UserRepository
import com.dgomesdev.taskslist.domain.usecase.token.DeleteTokenUseCase
import kotlinx.coroutines.flow.single

class DeleteUserUseCase(
    private val userRepository: UserRepository,
    private val deleteTokenUseCase: DeleteTokenUseCase,
    ) {
    suspend operator fun invoke(userId: String): Pair<User?, String> {
        val response = userRepository.deleteUser(userId).single().message.toString()
        deleteTokenUseCase()
        return Pair(null, response)
    }
}