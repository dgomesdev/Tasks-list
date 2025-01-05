package com.dgomesdev.taskslist.presentation.ui.features.taskList

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.presentation.ui.app.ChooseTask
import com.dgomesdev.taskslist.presentation.ui.app.OnAction
import com.dgomesdev.taskslist.presentation.ui.app.ScreenNavigation
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent

@Composable
fun TaskOptions(
    onAction: OnAction,
    task: Task,
    goToScreen: ScreenNavigation,
    onChooseTask: ChooseTask,
    onOpenOptions: () -> Unit,
    areOptionsExpanded: Boolean
) {
    IconButton(
        onClick = { onOpenOptions() }
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.options)
        )
        DropdownMenu(expanded = areOptionsExpanded, onDismissRequest = { onOpenOptions() }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.edit_task)) },
                onClick = { onChooseTask(task) ; goToScreen("TaskDetails"); onOpenOptions() }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete_task)) },
                onClick = { onAction(AppUiIntent.DeleteTask(task)) ; onOpenOptions() }
            )
        }
    }
}