package com.dgomesdev.taskslist.domain.repository

import com.dgomesdev.taskslist.data.dto.request.AuthRequestDto
import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.data.dto.response.AuthResponseDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.UserResponseDto
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: AuthRequestDto): Flow<AuthResponseDto>
    suspend fun loginUser(user: AuthRequestDto): Flow<AuthResponseDto>
    suspend fun getUser(userId: String): Flow<UserResponseDto>
    suspend fun updateUser(userId: String, user: UserRequestDto): Flow<UserResponseDto>
    suspend fun deleteUser(userId: String): Flow<MessageDto>
}