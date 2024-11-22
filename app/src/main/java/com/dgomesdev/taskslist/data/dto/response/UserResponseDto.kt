package com.dgomesdev.taskslist.data.dto.response

import java.util.UUID

data class UserResponseDto(
    val userId: UUID,
    val username: String,
    val password: String,
    val tasks: List<TaskResponseDto>
)
