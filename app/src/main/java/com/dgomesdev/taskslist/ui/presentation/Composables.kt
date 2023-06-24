package com.dgomesdev.taskslist.ui.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.TaskEntity

typealias AddTask = (task: TaskEntity) -> Unit
typealias EditTask = (task: TaskEntity) -> Unit
typealias DeleteTask = (task: TaskEntity) -> Unit
typealias RefreshList = () -> Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskApp(
    tasks: List<TaskEntity>,
    addTask: AddTask,
    editTask: EditTask,
    deleteTask: DeleteTask,
    refreshTaskList: RefreshList
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
        topBar = { AppBar() },
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
            refreshList = refreshTaskList
        )
    }
}

@Composable
fun NewTaskButton(
    goToScreen: ScreenNavigation
) {
    FloatingActionButton(
        onClick = { goToScreen("Add task screen") },
        modifier = Modifier
            .padding(all = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_new_task_button)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    val context = LocalContext.current
    TopAppBar(
        title = { Text(stringResource(R.string.tasks_list)) },
        actions = {
            IconButton(onClick = {
                Toast.makeText(
                    context,
                    "Developed by Dgomes Dev",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Developed by Dgomes Dev"
                )
            }
        })
}