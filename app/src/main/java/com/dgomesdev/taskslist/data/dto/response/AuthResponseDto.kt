package com.dgomesdev.taskslist.data.dto.response

data class AuthResponseDto(
    val user: UserResponseDto,
    val token: String
)
