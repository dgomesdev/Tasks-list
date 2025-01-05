package com.dgomesdev.taskslist.presentation.ui.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.presentation.ui.theme.TasksListTheme
import com.dgomesdev.taskslist.presentation.viewmodel.TasksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val tasksViewModel: TasksViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by tasksViewModel.uiState.collectAsState()
            val snackbarHostState = remember { tasksViewModel.snackbarHostState }
            TasksListTheme {
                AppNavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    uiState = uiState,
                    onAction = tasksViewModel::handleAppUiIntent,
                    snackbarHostState = snackbarHostState,
                    showSnackbar = tasksViewModel::showSnackbar
                )
            }
        }
    }
}