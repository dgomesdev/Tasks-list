package com.dgomesdev.taskslist.domain.usecase.user

import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.UserRepository
import kotlinx.coroutines.flow.single

class UpdateUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User): Pair<User?, String> =
        Pair(
            User.fromApi(
                userRepository.updateUser(
                    user.userId!!,
                    UserRequestDto.create(user)
                ).single()
            ),
            "User updated successfully"
        )
}