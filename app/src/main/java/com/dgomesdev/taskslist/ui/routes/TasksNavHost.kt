package com.dgomesdev.taskslist.ui.routes

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dgomesdev.taskslist.domain.TaskEntity
import com.dgomesdev.taskslist.ui.composables.AddTask
import com.dgomesdev.taskslist.ui.composables.DeleteTask
import com.dgomesdev.taskslist.ui.composables.EditTask
import com.dgomesdev.taskslist.ui.presentation.screens.AddTaskScreen
import com.dgomesdev.taskslist.ui.presentation.screens.EditTaskScreen
import com.dgomesdev.taskslist.ui.presentation.screens.MainScreen

typealias ScreenNavigation = (String) -> Unit
@Composable
fun TasksNavHost(
    navController: NavHostController,
    tasks: List<TaskEntity>,
    addTask: AddTask,
    editTask: EditTask,
    deleteTask: DeleteTask,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "Main screen",
        modifier = modifier.padding(8.dp)
    ) {
        composable(route = "Main screen") {
            MainScreen(
                tasks = tasks,
                deleteTask = deleteTask,
                screenNavigation = { navController.navigate(it) },
                modifier = modifier,
                setStatus = editTask
            )
        }
        composable(route = "Add task screen") {
            AddTaskScreen(
                addTask = addTask,
                goToScreen = { navController.navigate(it) }
            )
        }
        composable(
            route = "Edit task screen/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { it ->
            val taskId = it.arguments?.getInt("taskId")
            val taskToBeEdited = tasks.firstOrNull { task ->
                task.id == taskId }
            EditTaskScreen(
                editTask = editTask,
                task = taskToBeEdited!!,
                goToScreen = { navController.navigate(it) }
            )
        }
    }
}
