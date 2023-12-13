package com.dgomesdev.taskslist.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.ui.routes.ScreenNavigation

@Composable
fun NewTaskButton(
    goToScreen: ScreenNavigation
) {
    FloatingActionButton(
        onClick = { goToScreen("Add task screen") },
        modifier = Modifier
            .padding(all = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add new task button"
        )
    }
}