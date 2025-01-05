package com.dgomesdev.taskslist.di

import com.dgomesdev.taskslist.data.repository.TaskRepositoryImpl
import com.dgomesdev.taskslist.data.repository.UserRepositoryImpl
import com.dgomesdev.taskslist.data.service.HttpClient
import com.dgomesdev.taskslist.data.service.TaskService
import com.dgomesdev.taskslist.data.service.UserService
import com.dgomesdev.taskslist.data.service.impl.TaskServiceImpl
import com.dgomesdev.taskslist.data.service.impl.UserServiceImpl
import com.dgomesdev.taskslist.domain.repository.TaskRepository
import com.dgomesdev.taskslist.domain.repository.UserRepository
import com.dgomesdev.taskslist.domain.usecase.task.DeleteTaskUseCase
import com.dgomesdev.taskslist.domain.usecase.task.SaveTaskUseCase
import com.dgomesdev.taskslist.domain.usecase.task.UpdateTaskUseCase
import com.dgomesdev.taskslist.domain.usecase.user.DeleteUserUseCase
import com.dgomesdev.taskslist.domain.usecase.user.GetUserUseCase
import com.dgomesdev.taskslist.domain.usecase.user.UpdateUserUseCase
import com.dgomesdev.taskslist.domain.usecase.user.RegisterUseCase
import com.dgomesdev.taskslist.domain.usecase.user.LoginUseCase
import com.dgomesdev.taskslist.domain.usecase.user.RecoverPasswordUseCase
import com.dgomesdev.taskslist.domain.usecase.user.ResetPasswordUseCase
import com.dgomesdev.taskslist.domain.usecase.UserUseCases
import com.dgomesdev.taskslist.domain.usecase.TaskUseCases
import com.dgomesdev.taskslist.infra.SecurePreferences
import com.dgomesdev.taskslist.presentation.viewmodel.TasksViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::TaskRepositoryImpl) { bind<TaskRepository>() }

    singleOf(::UserServiceImpl) { bind<UserService>() }
    singleOf(::TaskServiceImpl) { bind<TaskService>() }

    viewModelOf(::TasksViewModel)

    // Use Cases - Task
    singleOf(::SaveTaskUseCase)
    singleOf(::UpdateTaskUseCase)
    singleOf(::DeleteTaskUseCase)

    // Use Cases - User
    singleOf(::RegisterUseCase)
    singleOf(::LoginUseCase)
    singleOf(::GetUserUseCase)
    singleOf(::UpdateUserUseCase)
    singleOf(::DeleteUserUseCase)
    singleOf(::RecoverPasswordUseCase)
    singleOf(::ResetPasswordUseCase)

    singleOf(::UserUseCases)
    singleOf(::TaskUseCases)

    singleOf(::SecurePreferences)
    singleOf(::HttpClient)
}