package com.dgomesdev.taskslist.ui.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dgomesdev.taskslist.domain.TaskEntity
import com.dgomesdev.taskslist.ui.presentation.AddTask
import com.dgomesdev.taskslist.ui.presentation.screens.AddTaskScreen
import com.dgomesdev.taskslist.ui.presentation.DeleteTask
import com.dgomesdev.taskslist.ui.presentation.EditTask
import com.dgomesdev.taskslist.ui.presentation.screens.EditTaskScreen
import com.dgomesdev.taskslist.ui.presentation.screens.MainScreen
import com.dgomesdev.taskslist.ui.presentation.RefreshList
import com.dgomesdev.taskslist.ui.presentation.ValidateEndDate

typealias ScreenNavigation = (String) -> Unit
@Composable
fun TasksNavHost(
    navController: NavHostController,
    tasks: List<TaskEntity>,
    addTask: AddTask,
    editTask: EditTask,
    deleteTask: DeleteTask,
    refreshList: RefreshList,
    validateEndDate: ValidateEndDate,
    isEndDateValid: Boolean,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "Main screen",
        modifier = modifier
    ) {
        composable(route = "Main screen") {
            MainScreen(
                tasks = tasks,
                deleteTask = deleteTask,
                screenNavigation = { navController.navigate(it) },
                modifier = modifier
            )
        }
        composable(route = "Add task screen") {
            AddTaskScreen(
                addTask = addTask,
                refreshList = refreshList,
                goToScreen = { navController.navigate(it) },
                validateEndDate = validateEndDate,
                isEndDateValid = isEndDateValid
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
                goToScreen = { navController.navigate(it) },
                validateEndDate = validateEndDate,
                isEndDateValid = isEndDateValid
            )
        }
    }
}
