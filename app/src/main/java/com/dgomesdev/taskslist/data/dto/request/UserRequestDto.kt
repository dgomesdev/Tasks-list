package com.dgomesdev.taskslist.data.dto.request

import com.dgomesdev.taskslist.domain.model.User

data class UserRequestDto(val user: User) {
    val username: String = user.username
    val password: String = user.password
}