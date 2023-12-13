package com.dgomesdev.taskslist.ui.composables

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.dgomesdev.taskslist.domain.TaskEntity
import com.dgomesdev.taskslist.ui.presentation.DeleteTask
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
        DropdownMenuItem(text = { Text(if (isTaskDone) "Unmark as done" else "Mark as done") }, onClick = { onMarkAsDone() ; onExpandChange(false) })
        DropdownMenuItem(text = { Text("Edit task") }, onClick = { goToScreen("Edit task screen/${task.id}") ; onExpandChange(false) })
        DropdownMenuItem(text = { Text("Delete task") }, onClick = { deleteTask(task) ; onExpandChange(false) } )
    }
}