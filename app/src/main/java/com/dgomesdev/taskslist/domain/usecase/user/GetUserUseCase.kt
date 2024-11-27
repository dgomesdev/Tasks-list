package com.dgomesdev.taskslist.domain.usecase.user

import android.content.Context
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.repository.UserRepository
import kotlinx.coroutines.flow.single

class GetUserUseCase(private val userRepository: UserRepository, private val context: Context) {
    suspend operator fun invoke(userId: String): Pair<User?, String> =
        try {
            val user = User.fromApi(userRepository.getUser(userId).single())
            Pair(user, context.getString(R.string.hello, user.username))
        } catch (e: Exception) {
            Pair(null, e.message ?: context.getString(R.string.user_not_authenticated))
        }
}