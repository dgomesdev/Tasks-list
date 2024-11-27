package com.dgomesdev.taskslist.domain.usecase.user

import android.content.Context
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.UserRepository
import kotlinx.coroutines.flow.single

class UpdateUserUseCase(private val userRepository: UserRepository, private val context: Context) {
    suspend operator fun invoke(user: User): Pair<User?, String> =
        Pair(
            User.fromApi(
                userRepository.updateUser(
                    user.userId!!,
                    UserRequestDto.create(user)
                ).single()
            ),
            context.getString(R.string.user_updated)
        )
}