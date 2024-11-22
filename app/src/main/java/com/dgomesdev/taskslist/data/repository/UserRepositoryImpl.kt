package com.dgomesdev.taskslist.data.repository

import com.dgomesdev.taskslist.data.dto.request.AuthRequestDto
import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.data.dto.response.AuthResponseDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.UserResponseDto
import com.dgomesdev.taskslist.data.service.UserService
import com.dgomesdev.taskslist.domain.repository.UserRepository
import java.util.UUID

class UserRepositoryImpl(private val userService: UserService): UserRepository {
    override suspend fun saveUser(user: AuthRequestDto): AuthResponseDto =
        userService.registerUser(user)

    override suspend fun loginUser(user: AuthRequestDto): AuthResponseDto =
        userService.loginUser(user)

    override suspend fun getUser(userId: UUID): UserResponseDto =
        userService.getUser(userId)

    override suspend fun updateUser(userId: UUID, user: UserRequestDto): UserResponseDto =
        userService.updateUser(userId, user)

    override suspend fun deleteUser(userId: UUID): MessageDto =
        userService.deleteUser(userId)
}
