package com.dgomesdev.taskslist.presentation.ui.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TaskNavigationDrawer(
    modifier: Modifier = Modifier,
    goToScreen: ScreenNavigation
) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Text("Drawer title", modifier = modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "Edit profile") },
                    selected = false,
                    onClick = { goToScreen("UserDetails") }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Change language") },
                    selected = false,
                    onClick = { TODO() }
                )
            }
        }
    ) {
        // Screen content
    }
}