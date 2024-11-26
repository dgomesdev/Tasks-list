package com.dgomesdev.taskslist.presentation.ui.app

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.model.TaskAction
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.model.UserAction
import com.dgomesdev.taskslist.presentation.ui.features.auth.AuthScreen
import com.dgomesdev.taskslist.presentation.ui.features.taskDetails.TaskDetailsScreen
import com.dgomesdev.taskslist.presentation.ui.features.taskList.MainScreen
import com.dgomesdev.taskslist.presentation.ui.features.userDetails.UserDetailsScreen
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState

typealias HandleTaskAction = (TaskAction, Task) -> Unit
typealias HandleUserAction = (UserAction, User) -> Unit
typealias ScreenNavigation = (String) -> Unit
typealias ChooseTask = (Task) -> Unit

@Composable
fun TasksApp(
    modifier: Modifier = Modifier,
    uiState: AppUiState
) {
    val navController = rememberNavController()
    var task by rememberSaveable { mutableStateOf<Task?>(null) }

    val startDestination = when {
        uiState.user != null -> "TaskList"
        else -> "Auth"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier.padding(8.dp)
    ) {
        composable(route = "TaskList") {
            MainScreen(
                uiState = uiState,
                handleTaskAction = uiState.onTaskChange,
                goToScreen = {
                    navController.navigate(it)
                },
                onChooseTask = { task = it }
            )
        }
        composable(route = "TaskDetails") {
            TaskDetailsScreen(
                handleTaskAction = uiState.onTaskChange,
                task = task,
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
