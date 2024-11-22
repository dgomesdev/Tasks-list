package com.dgomesdev.taskslist.presentation.ui.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewTaskButton(
    goToScreen: ScreenNavigation
) {
    FloatingActionButton(
        onClick = { goToScreen("TaskDetails") },
        modifier = Modifier
            .padding(all = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add new task button"
        )
    }
}