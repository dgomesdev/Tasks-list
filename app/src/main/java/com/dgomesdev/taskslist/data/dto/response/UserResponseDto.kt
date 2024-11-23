package com.dgomesdev.taskslist.data.dto.response

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserResponseDto(
    val userId: String,
    val username: String,
    val password: String,
    val tasks: List<TaskResponseDto>
)
