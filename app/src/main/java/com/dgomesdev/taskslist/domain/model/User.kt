package com.dgomesdev.taskslist.domain.model

import com.dgomesdev.taskslist.data.dto.response.UserResponseDto
import java.util.UUID

data class User(
    val userId: UUID? = null,
    val username: String,
    val password: String,
    val tasks: List<Task> = emptyList(),
    val userAuthorities: Set<UserAuthority> = setOf(UserAuthority.USER)
) {
    companion object {
        fun fromApi(user: UserResponseDto) = User(
            userId = user.userId,
            username = user.username,
            password = user.password,
            tasks = user.tasks.map { Task.fromApi(it) },
            userAuthorities = setOf(UserAuthority.USER),
        )
    }
}
