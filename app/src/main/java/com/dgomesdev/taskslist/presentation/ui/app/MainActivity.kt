package com.dgomesdev.taskslist.presentation.ui.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.dgomesdev.taskslist.presentation.ui.theme.TasksListTheme
import com.dgomesdev.taskslist.presentation.viewmodel.TasksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val tasksViewModel: TasksViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by tasksViewModel.uiState.collectAsState()
            TasksListTheme {
                    TaskApp(uiState)
            }
        }
    }
}