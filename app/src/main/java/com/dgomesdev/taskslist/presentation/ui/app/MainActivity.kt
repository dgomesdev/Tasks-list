package com.dgomesdev.taskslist.presentation.ui.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.presentation.ui.theme.TasksListTheme
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent
import com.dgomesdev.taskslist.presentation.viewmodel.TasksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

const val EMAIL_PATTERN = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
class MainActivity : ComponentActivity() {

    private val tasksViewModel: TasksViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by tasksViewModel.uiState.collectAsState()

            intent?.data?.let { uri ->
                Log.i("URI INTENT", "$uri")
                val code = uri.getQueryParameter("code")
                if (code != null) tasksViewModel.handleAppUiIntent(AppUiIntent.SetRecoveryCode(code))
            }

            TasksListTheme {
                AppNavHost(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    uiState = uiState,
                    onAction = tasksViewModel::handleAppUiIntent,
                    activity = this
                )
            }
        }
    }
}