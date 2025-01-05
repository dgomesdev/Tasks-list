package com.dgomesdev.taskslist.data.dto.request

import com.dgomesdev.taskslist.domain.model.User
import kotlinx.serialization.Serializable

@ConsistentCopyVisibility
@Serializable
data class UserRequestDto private constructor(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null
) {
    companion object {
        fun register(user: User) = UserRequestDto(
            username = user.username,
            email = user.email,
            password = user.password
        )

        fun login(user: User) = UserRequestDto(
            email = user.email,
            password = user.password
        )

        fun update(user: User) = UserRequestDto(
            username = user.username,
            email = user.email,
            password = user.password
        )

        fun recoverPassword(user: User) = UserRequestDto(
            email = user.email
        )

        fun resetPassword(user: User) = UserRequestDto(
            email = user.email,
            password = user.password
        )
    }
}
