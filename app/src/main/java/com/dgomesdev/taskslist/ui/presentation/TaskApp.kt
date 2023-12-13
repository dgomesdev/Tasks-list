package com.dgomesdev.taskslist.ui.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dgomesdev.taskslist.domain.TaskEntity
import com.dgomesdev.taskslist.ui.composables.NewTaskButton
import com.dgomesdev.taskslist.ui.composables.TaskAppBar
import com.dgomesdev.taskslist.ui.routes.TasksNavHost

typealias AddTask = (task: TaskEntity) -> Unit
typealias EditTask = (task: TaskEntity) -> Unit
typealias DeleteTask = (task: TaskEntity) -> Unit
typealias RefreshList = () -> Unit
typealias ValidateEndDate = (String?, String?) -> Unit

@Composable
fun TaskApp(
    tasks: List<TaskEntity>,
    addTask: AddTask,
    editTask: EditTask,
    deleteTask: DeleteTask,
    refreshTaskList: RefreshList,
    validateEndDate: ValidateEndDate,
    isEndDateValid: Boolean
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route
    var fabVisibility by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(currentDestination) {
        fabVisibility = currentDestination == "Main screen"
    }

    Scaffold(
        topBar = { TaskAppBar() },
        floatingActionButton = {
            if (fabVisibility) NewTaskButton(
                goToScreen = { navController.navigate(it) }
            )
        }
    ) { padding ->
        TasksNavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            tasks = tasks,
            addTask = addTask,
            editTask = editTask,
            deleteTask = deleteTask,
            refreshList = refreshTaskList,
            validateEndDate = validateEndDate,
            isEndDateValid = isEndDateValid
        )
    }
}