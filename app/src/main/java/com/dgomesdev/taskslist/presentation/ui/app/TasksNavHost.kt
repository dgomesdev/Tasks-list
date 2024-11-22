package com.dgomesdev.taskslist.presentation.ui.app

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dgomesdev.taskslist.presentation.ui.features.auth.AuthScreen
import com.dgomesdev.taskslist.presentation.ui.features.taskDetails.TaskDetailsScreen
import com.dgomesdev.taskslist.presentation.ui.features.taskList.TaskList
import com.dgomesdev.taskslist.presentation.ui.features.userDetails.UserDetailsScreen
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState

typealias ScreenNavigation = (String) -> Unit

@Composable
fun TasksNavHost(
    modifier: Modifier,
    navController: NavHostController,
    uiState: AppUiState
) {
    NavHost(
        navController = navController,
        startDestination = "TaskList",
        modifier = modifier.padding(8.dp)
    ) {
        composable(route = "TaskList") {
            TaskList(
                taskList = uiState.user?.tasks,
                handleTaskAction = uiState.onTaskChange,
                goToScreen = {
                    navController.navigate(it)
                },
                modifier = modifier
            )
        }
        composable(route = "TaskDetails") {
            TaskDetailsScreen(
                handleTaskAction = uiState.onTaskChange,
                task = uiState.task,
                goToScreen = {
                    navController.navigate(it)
                }
            )
        }
        composable(route = "UserDetails") {
            UserDetailsScreen(
                modifier,
                uiState.user!!,
                uiState.onUserChange
            )
        }
        composable(route = "Auth") {
            AuthScreen(
                modifier,
                uiState.onUserChange
            )
        }
    }
}
