package com.dgomesdev.taskslist.ui.composables

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.TaskEntity
import com.dgomesdev.taskslist.ui.routes.ScreenNavigation

@Composable
fun TaskOptions(
    expandedMenu: Boolean,
    onExpandChange: (Boolean) -> Unit,
    deleteTask: DeleteTask,
    task: TaskEntity,
    goToScreen: ScreenNavigation,
    isTaskDone: Boolean,
    onMarkAsDone: () -> Unit
) {
    DropdownMenu(expanded = expandedMenu, onDismissRequest = { onExpandChange(false) }) {
        DropdownMenuItem(
            text = { Text(
                if (isTaskDone) stringResource(R.string.unmark_as_done)
                else stringResource(R.string.mark_as_done)
            ) },
            onClick = { onMarkAsDone() ; onExpandChange(false) }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.edit_task)) },
            onClick = { goToScreen("Edit task screen/${task.id}") ; onExpandChange(false) }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.delete_task)) },
            onClick = { deleteTask(task) ; onExpandChange(false) }
        )
    }
}