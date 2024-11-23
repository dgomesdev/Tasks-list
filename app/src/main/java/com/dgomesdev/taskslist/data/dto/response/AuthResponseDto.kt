package com.dgomesdev.taskslist.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val user: UserResponseDto,
    val token: String
)
