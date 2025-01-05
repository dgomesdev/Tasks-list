package com.dgomesdev.taskslist.domain.usecase

import com.dgomesdev.taskslist.domain.usecase.user.ResetPasswordUseCase
import com.dgomesdev.taskslist.domain.usecase.user.DeleteUserUseCase
import com.dgomesdev.taskslist.domain.usecase.user.GetUserUseCase
import com.dgomesdev.taskslist.domain.usecase.user.LoginUseCase
import com.dgomesdev.taskslist.domain.usecase.user.RecoverPasswordUseCase
import com.dgomesdev.taskslist.domain.usecase.user.RegisterUseCase
import com.dgomesdev.taskslist.domain.usecase.user.UpdateUserUseCase

data class UserUseCases(
    val registerUseCase: RegisterUseCase,
    val loginUseCase: LoginUseCase,
    val getUserUseCase: GetUserUseCase,
    val updateUserUseCase: UpdateUserUseCase,
    val deleteUserUseCase: DeleteUserUseCase,
    val recoverPasswordUseCase: RecoverPasswordUseCase,
    val resetPasswordUseCase: ResetPasswordUseCase
)
