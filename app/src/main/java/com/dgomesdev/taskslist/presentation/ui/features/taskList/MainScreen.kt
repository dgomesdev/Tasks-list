package com.dgomesdev.taskslist.presentation.ui.features.taskList

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.UserAction
import com.dgomesdev.taskslist.presentation.ui.app.ChooseTask
import com.dgomesdev.taskslist.presentation.ui.app.ScreenNavigation
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier,
    uiState: AppUiState,
    goToScreen: ScreenNavigation,
    onChooseTask: ChooseTask
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.fillMaxWidth() ,horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.business_card),
                        contentDescription = stringResource(R.string.app_name)
                    )
                    Text("Task list app", modifier = Modifier.padding(16.dp))
                }

                HorizontalDivider()

                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.edit_profile)) },
                    selected = false,
                    onClick = { goToScreen("UserDetails"); scope.launch { drawerState.close() } }
                )

                NavigationDrawerItem(
                    label = { Text(text = stringResource(R.string.log_out)) },
                    selected = false,
                    onClick = { uiState.onLogout() ; scope.launch { drawerState.close() } }
                )

                NavigationDrawerItem(
                    label = { Text(text = stringResource(R.string.delete_account)) },
                    selected = false,
                    onClick = { uiState.onUserChange(UserAction.DELETE, uiState.user!!); scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        TaskList(
            modifier = modifier,
            uiState = uiState,
            goToScreen = goToScreen,
            onChooseTask = onChooseTask,
            onOpenDrawer = { scope.launch { drawerState.open() } }
        )
    }
}

@Preview
@Composable
private fun MainPrev() {
    MainScreen(
        modifier = Modifier,
        uiState = AppUiState(),
        goToScreen = {},
        onChooseTask = {}
    )
}