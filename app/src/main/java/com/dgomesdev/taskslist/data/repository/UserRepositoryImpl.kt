package com.dgomesdev.taskslist.data.repository

import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.UserResponseDto
import com.dgomesdev.taskslist.data.service.UserService
import com.dgomesdev.taskslist.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(private val userService: UserService): UserRepository {
    override suspend fun registerUser(user: UserRequestDto): Flow<UserResponseDto> =
        flow { emit(userService.registerUser(user).getOrElse { error(it) }) }

    override suspend fun loginUser(user: UserRequestDto): Flow<UserResponseDto> =
        flow { emit(userService.loginUser(user).getOrElse { error(it) }) }

    override suspend fun getUser(userId: String): Flow<UserResponseDto> =
        flow { emit(userService.getUser(userId).getOrElse { error(it) }) }

    override suspend fun updateUser(userId: String, user: UserRequestDto): Flow<UserResponseDto> =
        flow { emit(userService.updateUser(userId, user).getOrElse { error(it) }) }

    override suspend fun deleteUser(userId: String): Flow<MessageDto> =
        flow { emit(userService.deleteUser(userId).getOrElse { error(it) }) }

    override suspend fun recoverPassword(user: UserRequestDto): Flow<MessageDto> =
        flow { emit(userService.recoverPassword(user).getOrElse { error(it) }) }

    override suspend fun resetPassword(recoveryCode: String, user: UserRequestDto): Flow<MessageDto> =
        flow { emit(userService.resetPassword(recoveryCode, user).getOrElse { error(it) }) }
}
