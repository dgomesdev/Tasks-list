package com.dgomesdev.taskslist.presentation.ui.features.taskList

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.presentation.ui.app.ChooseTask
import com.dgomesdev.taskslist.presentation.ui.app.HandleTaskAction
import com.dgomesdev.taskslist.presentation.ui.app.ScreenNavigation
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: AppUiState,
    handleTaskAction: HandleTaskAction,
    goToScreen: ScreenNavigation,
    onChooseTask: ChooseTask
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Text("Task list app", modifier = modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "Edit profile") },
                    selected = false,
                    onClick = { goToScreen("UserDetails"); scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Change language") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Logout") },
                    selected = false,
                    onClick = { uiState.onLogout() ; scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        TaskList(
            uiState = uiState,
            handleTaskAction = handleTaskAction,
            goToScreen = goToScreen,
            onChooseTask = onChooseTask,
            onOpenDrawer = { scope.launch { drawerState.apply { open() } } }
        )
    }
}