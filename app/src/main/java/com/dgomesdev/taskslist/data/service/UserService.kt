package com.dgomesdev.taskslist.data.service

import com.dgomesdev.taskslist.data.dto.request.AuthRequestDto
import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.data.dto.response.AuthResponseDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.UserResponseDto

interface UserService {
    suspend fun registerUser(user: AuthRequestDto): Result<AuthResponseDto>
    suspend fun loginUser(user: AuthRequestDto): Result<AuthResponseDto>
    suspend fun getUser(userId: String): Result<UserResponseDto>
    suspend fun updateUser(userId: String, user: UserRequestDto): Result<UserResponseDto>
    suspend fun deleteUser(userId: String): Result<MessageDto>
}