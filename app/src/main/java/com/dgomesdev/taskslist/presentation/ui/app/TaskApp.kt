package com.dgomesdev.taskslist.presentation.ui.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.model.TaskAction
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.model.UserAction
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState

typealias HandleTaskAction = (TaskAction, Task) -> Unit
typealias HandleUserAction = (UserAction, User) -> Unit

@Composable
fun TaskApp(
    uiState: AppUiState
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route
    var fabVisibility by remember {
        mutableStateOf(true)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    LaunchedEffect(currentDestination) {
        fabVisibility = currentDestination == "TaskList"
    }

    Scaffold(
        topBar = { TaskAppBar( ) },
        floatingActionButton = {
            if (fabVisibility) NewTaskButton(
                goToScreen = { navController.navigate(it) }
            )
        }
    ) { padding ->
        TasksNavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            uiState = uiState
        )
    }
}