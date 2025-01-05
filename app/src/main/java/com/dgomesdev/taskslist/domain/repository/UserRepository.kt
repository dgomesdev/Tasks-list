package com.dgomesdev.taskslist.domain.repository

import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.UserResponseDto
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun registerUser(user: UserRequestDto): Flow<UserResponseDto>
    suspend fun loginUser(user: UserRequestDto): Flow<UserResponseDto>
    suspend fun getUser(userId: String): Flow<UserResponseDto>
    suspend fun updateUser(userId: String, user: UserRequestDto): Flow<UserResponseDto>
    suspend fun deleteUser(userId: String): Flow<MessageDto>
    suspend fun recoverPassword(user: UserRequestDto): Flow<MessageDto>
    suspend fun resetPassword(recoveryCode: String, user: UserRequestDto): Flow<MessageDto>
}