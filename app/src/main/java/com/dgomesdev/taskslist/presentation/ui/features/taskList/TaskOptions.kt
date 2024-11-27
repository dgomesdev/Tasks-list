package com.dgomesdev.taskslist.presentation.ui.features.taskList

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.model.TaskAction
import com.dgomesdev.taskslist.presentation.ui.app.ChooseTask
import com.dgomesdev.taskslist.presentation.ui.app.HandleTaskAction
import com.dgomesdev.taskslist.presentation.ui.app.ScreenNavigation

@Composable
fun TaskOptions(
    handleTaskAction: HandleTaskAction,
    task: Task,
    goToScreen: ScreenNavigation,
    onChooseTask: ChooseTask
) {
    var expandedMenu by remember { mutableStateOf(false) }

    IconButton(
        onClick = { expandedMenu = true }
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.options)
        )
        DropdownMenu(expanded = expandedMenu, onDismissRequest = { expandedMenu = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.edit_task)) },
                onClick = { onChooseTask(task) ; goToScreen("TaskDetails"); expandedMenu = false }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete_task)) },
                onClick = { handleTaskAction(TaskAction.DELETE, task); expandedMenu = false }
            )
        }
    }
}