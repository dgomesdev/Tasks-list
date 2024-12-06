package com.dgomesdev.taskslist.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    val userId: String,
    val username: String,
    val password: String,
    val tasks: List<TaskResponseDto>
)
