package com.dgomesdev.taskslist.data.dto.request

import com.dgomesdev.taskslist.domain.model.User
import kotlinx.serialization.Serializable

@ConsistentCopyVisibility
@Serializable
data class AuthRequestDto private constructor(
    val username: String,
    val password: String
) {
    companion object {
        fun create(user: User) = AuthRequestDto(user.username, user.password)
    }
}