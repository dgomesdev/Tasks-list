package com.dgomesdev.taskslist.presentation.ui.app

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dgomesdev.taskslist.domain.model.Task
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.dgomesdev.taskslist.presentation.ui.features.auth.AuthScreen
import com.dgomesdev.taskslist.presentation.ui.features.taskDetails.TaskDetailsScreen
import com.dgomesdev.taskslist.presentation.ui.features.taskList.TaskList
import com.dgomesdev.taskslist.presentation.ui.features.userDetails.UserDetailsScreen
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState

typealias ScreenNavigation = (String) -> Unit
typealias ChooseTask = (Task) -> Unit

@Composable
fun TasksNavHost(
    modifier: Modifier,
    navController: NavHostController,
    uiState: AppUiState
) {
    var task by rememberSaveable { mutableStateOf<Task?>(null) }

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
