package com.dgomesdev.taskslist.presentation.ui.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.R

@Composable
fun NewTaskButton(
    goToScreen: ScreenNavigation,
    onChooseTask: ChooseTask
) {
    FloatingActionButton(
        onClick = { onChooseTask(null) ; goToScreen("TaskDetails") },
        modifier = Modifier
            .padding(all = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_new_task)
        )
    }
}