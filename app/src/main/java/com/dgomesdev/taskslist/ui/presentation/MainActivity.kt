package com.dgomesdev.taskslist.ui.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.dgomesdev.taskslist.domain.TaskEntity
import com.dgomesdev.taskslist.ui.theme.TasksListTheme

class MainActivity : ComponentActivity() {

    private val tasksViewModel: TaskViewModel by viewModels { TaskViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = tasksViewModel.uiState.collectAsState().value
            TasksListTheme {
                // A surface container using the 'background' color from the theme
                TaskApp(
                    tasks = state.tasks,
                    addTask = tasksViewModel::addTask,
                    editTask = tasksViewModel::editTask,
                    deleteTask = tasksViewModel::deleteTask,
                    refreshTaskList = tasksViewModel::getTaskList
                )
            }
        }
    }
}