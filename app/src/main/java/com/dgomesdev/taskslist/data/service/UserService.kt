package com.dgomesdev.taskslist.data.service

import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.UserResponseDto

interface UserService {
    suspend fun registerUser(user: UserRequestDto): Result<UserResponseDto>
    suspend fun loginUser(user: UserRequestDto): Result<UserResponseDto>
    suspend fun getUser(userId: String): Result<UserResponseDto>
    suspend fun updateUser(userId: String, user: UserRequestDto): Result<UserResponseDto>
    suspend fun deleteUser(userId: String): Result<MessageDto>
    suspend fun recoverPassword(user: UserRequestDto): Result<MessageDto>
    suspend fun resetPassword(recoveryCode: String, user: UserRequestDto): Result<MessageDto>
}