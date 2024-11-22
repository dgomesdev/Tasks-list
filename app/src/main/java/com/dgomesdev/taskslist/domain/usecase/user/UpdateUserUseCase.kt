package com.dgomesdev.taskslist.domain.usecase.user

import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.UserRepository

class UpdateUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User): User =
        User.fromApi(userRepository.updateUser(user.userId!!, UserRequestDto(user)))
}