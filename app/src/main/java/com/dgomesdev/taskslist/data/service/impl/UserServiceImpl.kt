package com.dgomesdev.taskslist.data.service.impl

import com.dgomesdev.taskslist.data.dto.request.AuthRequestDto
import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.data.dto.response.AuthResponseDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.UserResponseDto
import com.dgomesdev.taskslist.data.service.UserService
import com.dgomesdev.taskslist.infra.SecurePreferences
import java.util.UUID

class UserServiceImpl(securePreferences: SecurePreferences): UserService {
    override fun registerUser(user: AuthRequestDto): AuthResponseDto {
        TODO("Not yet implemented")
    }

    override fun loginUser(user: AuthRequestDto): AuthResponseDto {
        TODO("Not yet implemented")
    }

    override fun getUser(userId: UUID): UserResponseDto {
        TODO("Not yet implemented")
    }

    override fun updateUser(userId: UUID, user: UserRequestDto): UserResponseDto {
        TODO("Not yet implemented")
    }

    override fun deleteUser(userId: UUID): MessageDto {
        TODO("Not yet implemented")
    }
}