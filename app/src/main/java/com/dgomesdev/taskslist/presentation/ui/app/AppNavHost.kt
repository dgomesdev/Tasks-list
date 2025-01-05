package com.dgomesdev.taskslist.presentation.ui.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
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
import com.dgomesdev.taskslist.presentation.ui.features.auth.LoginScreen
import com.dgomesdev.taskslist.presentation.ui.features.auth.RegisterScreen
import com.dgomesdev.taskslist.presentation.ui.features.forgotPassword.ForgotPasswordScreen
import com.dgomesdev.taskslist.presentation.ui.features.loading.LoadingScreen
import com.dgomesdev.taskslist.presentation.ui.features.resetPassword.ResetPasswordScreen
import com.dgomesdev.taskslist.presentation.ui.features.taskDetails.TaskDetailsScreen
import com.dgomesdev.taskslist.presentation.ui.features.taskList.NavigationDrawer
import com.dgomesdev.taskslist.presentation.ui.features.userDetails.UserDetailsScreen
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState

typealias ScreenNavigation = (String) -> Unit
typealias ChooseTask = (Task) -> Unit
typealias OnAction = (AppUiIntent) -> Unit
typealias ShowSnackbar = (String) -> Unit

@Composable
fun AppNavHost(
    modifier: Modifier,
    uiState: AppUiState,
    onAction: OnAction,
    snackbarHostState: SnackbarHostState,
    showSnackbar: ShowSnackbar
) {
    val navController = rememberNavController()
    var task by rememberSaveable { mutableStateOf<Task?>(null) }

    LaunchedEffect(uiState.user) {
        if (uiState.user == null) navController.navigate("Login")
        else navController.navigate("TaskList")
    }
    LaunchedEffect(uiState.isLoading) {
        if (uiState.isLoading) navController.navigate("Loading")
        else {
            if (uiState.user != null) navController.navigate("TaskList")
            else navController.navigate("Login")
        }

    }
    LaunchedEffect(uiState.recoveryCode) {
        uiState.recoveryCode?.let{
            navController.navigate("ResetPassword")
        }
    }

    NavHost(
        navController = navController,
        startDestination = "Login",
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
            NavigationDrawer(
                modifier = modifier,
                uiState = uiState,
                onAction = onAction,
                goToScreen = {
                    navController.navigate(it)
                },
                onChooseTask = { task = it },
                snackbarHostState = snackbarHostState,
                showSnackbar = showSnackbar
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
                onAction = onAction,
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
                onAction = onAction,
                backToMainScreen = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "Login",
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
            LoginScreen(
                modifier,
                onAction,
                goToScreen = { navController.navigate(it) },
                snackbarHostState = snackbarHostState,
                showSnackbar = showSnackbar,
                message = uiState.message
            )
        }
        composable(
            route = "Register",
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
            RegisterScreen(
                modifier,
                uiState.message,
                onAction,
                snackbarHostState = snackbarHostState,
                showSnackbar = showSnackbar,
                goToScreen = { navController.navigate(it) },
                backToMainScreen = { navController.popBackStack() }
            )
        }
        composable(
            route = "ForgotPassword",
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
            ForgotPasswordScreen(
                modifier,
                uiState.message,
                onAction,
                snackbarHostState = snackbarHostState,
                showSnackbar = showSnackbar,
                goToScreen = { navController.navigate(it) },
                backToMainScreen = { navController.popBackStack() }
            )
        }
        composable(
            route = "ResetPassword",
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
            ResetPasswordScreen(
                modifier,
                uiState.recoveryCode,
                uiState.message,
                onAction,
                snackbarHostState = snackbarHostState,
                showSnackbar = showSnackbar,
                goToScreen = { navController.navigate(it) },
                backToMainScreen = { navController.popBackStack("Login", false) }
            )
        }
        composable(
            "Loading"
        ) {
            LoadingScreen(modifier)
        }
    }
}
