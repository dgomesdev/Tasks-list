package com.dgomesdev.taskslist.domain.repository

import com.dgomesdev.taskslist.data.dto.request.AuthRequestDto
import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.data.dto.response.AuthResponseDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.UserResponseDto
import java.util.UUID

interface UserRepository {
    suspend fun saveUser(user: AuthRequestDto): AuthResponseDto
    suspend fun loginUser(user: AuthRequestDto): AuthResponseDto
    suspend fun getUser(userId: UUID): UserResponseDto
    suspend fun updateUser(userId: UUID, user: UserRequestDto): UserResponseDto
    suspend fun deleteUser(userId: UUID): MessageDto
}