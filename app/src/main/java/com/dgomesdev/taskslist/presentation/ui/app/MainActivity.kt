package com.dgomesdev.taskslist.presentation.ui.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.dgomesdev.taskslist.presentation.ui.features.loading.LoadingScreen
import com.dgomesdev.taskslist.presentation.ui.theme.TasksListTheme
import com.dgomesdev.taskslist.presentation.viewmodel.TasksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val tasksViewModel: TasksViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by tasksViewModel.uiState.collectAsState()
            val fillMaxSize = Modifier.fillMaxSize()
            TasksListTheme {
                if (uiState.isLoading) LoadingScreen(modifier = fillMaxSize)
                else TasksApp(modifier = fillMaxSize, uiState = uiState)
            }
        }
    }
}