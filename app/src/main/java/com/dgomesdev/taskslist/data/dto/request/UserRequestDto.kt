package com.dgomesdev.taskslist.data.dto.request

import com.dgomesdev.taskslist.domain.model.User
import kotlinx.serialization.Serializable

@ConsistentCopyVisibility
@Serializable
data class UserRequestDto private constructor(
    val username: String,
    val password: String
) {
    companion object {
        fun create(user: User) = UserRequestDto(user.username, user.password)
    }
}
