package com.dgomesdev.taskslist.data.service

import com.dgomesdev.taskslist.data.dto.request.AuthRequestDto
import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.data.dto.response.AuthResponseDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.UserResponseDto
import java.util.UUID

interface UserService {
    fun registerUser(user: AuthRequestDto): AuthResponseDto
    fun loginUser(user: AuthRequestDto): AuthResponseDto
    fun getUser(userId: UUID): UserResponseDto
    fun updateUser(userId: UUID, user: UserRequestDto): UserResponseDto
    fun deleteUser(userId: UUID): MessageDto
}