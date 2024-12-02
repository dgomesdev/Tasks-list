package com.dgomesdev.taskslist.presentation.ui.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.model.TaskAction
import com.dgomesdev.taskslist.presentation.ui.features.auth.AuthScreen
import com.dgomesdev.taskslist.presentation.ui.features.taskDetails.TaskDetailsScreen
import com.dgomesdev.taskslist.presentation.ui.features.taskList.MainScreen
import com.dgomesdev.taskslist.presentation.ui.features.userDetails.UserDetailsScreen
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState

typealias HandleTaskAction = (TaskAction, Task) -> Unit
typealias ScreenNavigation = (String) -> Unit
typealias ChooseTask = (Task) -> Unit

@Composable
fun TasksApp(
    modifier: Modifier,
    uiState: AppUiState
) {
    val navController = rememberNavController()
    var task by rememberSaveable { mutableStateOf<Task?>(null) }

    LaunchedEffect(uiState.user) {
        if (uiState.user == null) navController.navigate("Auth")
        else navController.navigate("TaskList")
    }

    NavHost(
        navController = navController,
        startDestination = "TaskList",
    ) {
        composable(
            route = "TaskList",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(700)
                )
            }
        ) {
            MainScreen(
                modifier = modifier,
                uiState = uiState,
                goToScreen = {
                    navController.navigate(it)
                    uiState.onRefreshMessage()
                },
                onChooseTask = { task = it }
            )
        }
        composable(
            route = "TaskDetails",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(700)
                )
            }
        ) {
            TaskDetailsScreen(
                modifier = modifier,
                handleTaskAction = uiState.onTaskChange,
                task = task,
                backToMainScreen = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "UserDetails",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(700)
                )
            }
        ) {
            UserDetailsScreen(
                modifier = modifier,
                uiState = uiState,
                backToMainScreen = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "Auth",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(700)
                )
            }
        ) {
            AuthScreen(
                modifier,
                uiState
            )
        }
    }
}
