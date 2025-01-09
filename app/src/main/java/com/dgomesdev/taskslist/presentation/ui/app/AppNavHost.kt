package com.dgomesdev.taskslist.presentation.ui.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.presentation.ui.features.auth.AccountManager
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
typealias ChooseTask = (Task?) -> Unit
typealias OnAction = (AppUiIntent) -> Unit

@Composable
fun AppNavHost(
    modifier: Modifier,
    uiState: AppUiState,
    onAction: OnAction,
    activity: MainActivity
) {
    val (task, setTask) = rememberSaveable { mutableStateOf<Task?>(null) }
    val navController = rememberNavController()
    val accountManager = remember { AccountManager(activity) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        when {
            uiState.isLoading -> navController.navigate("Loading")
            uiState.recoveryCode != null -> navController.navigate("ResetPassword")
            uiState.user != null -> navController.navigate("TaskList")
            uiState.message?.contains("406") == true -> navController.navigate("Register")
            else -> if (navController.currentDestination?.route != "Login") {
                navController.popBackStack("Login", false)
            } else Unit
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
                goToScreen = { navController.navigate(it) },
                onChooseTask = { setTask(it) },
                scope = scope,
                backToMainScreen = { navController.popBackStack("Login", false) },
                accountManager = accountManager
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
                backToMainScreen = { navController.popBackStack() }
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
                backToMainScreen = { navController.popBackStack() },
                accountManager = accountManager,
                scope = scope
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
                uiState,
                onAction,
                goToScreen = { navController.navigate(it) },
                accountManager,
                scope
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
                uiState,
                onAction,
                backToMainScreen = { navController.popBackStack() },
                accountManager = accountManager,
                scope = scope
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
                uiState,
                onAction,
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
                uiState,
                onAction,
                backToMainScreen = { navController.popBackStack("Login", false) }
            )
        }
        composable("Loading") {
            LoadingScreen(modifier)
        }
    }
}
