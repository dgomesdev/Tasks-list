package com.dgomesdev.taskslist.ui.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.TaskEntity

@Composable
fun MainScreen(
    tasks: List<TaskEntity>,
    deleteTask: DeleteTask,
    screenNavigation: ScreenNavigation,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        TaskList(taskList = tasks, modifier = modifier, deleteTask = deleteTask, screenNavigation = screenNavigation)
    }

}

@Composable
fun TaskList(
    taskList: List<TaskEntity>,
    modifier: Modifier,
    deleteTask: DeleteTask,
    screenNavigation: ScreenNavigation
) {
    LazyColumn {
        items(taskList) { task ->
            Task(task, deleteTask = deleteTask, goToScreen = screenNavigation)
        }
    }
}

@Composable
fun Task(
    task: TaskEntity,
    deleteTask: DeleteTask,
    goToScreen: ScreenNavigation
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var expandedMenu by remember {
        mutableStateOf(false)
    }
    var isTaskDone by rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(
                    task.name,
                    style = TextStyle(textDecoration = if (isTaskDone) TextDecoration.LineThrough else TextDecoration.None)
                )
                if (expanded) {
                    Text(task.description, modifier = Modifier.padding(top = 32.dp))
                }
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) stringResource(R.string.show_less)
                    else stringResource(R.string.show_more)
                )
            }
            IconButton(onClick = { expandedMenu = true }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = stringResource(R.string.task_options))
                TaskOptions(
                    expandedMenu = expandedMenu,
                    onExpandChange = { expandedMenu = it},
                    deleteTask = deleteTask,
                    task = task,
                    goToScreen = goToScreen,
                    isTaskDone = isTaskDone,
                    onMarkAsDone = { isTaskDone = !isTaskDone }
                )
            }
        }
    }
}

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
        DropdownMenuItem(text = { Text(if (isTaskDone) stringResource(R.string.unmark_as_done) else stringResource(
            R.string.mark_as_done
        )
        )}, onClick = { onMarkAsDone() ; onExpandChange(false) })
        DropdownMenuItem(text = { Text(stringResource(R.string.edit_task)) }, onClick = { goToScreen("Edit task screen/${task.id}") ; onExpandChange(false) })
        DropdownMenuItem(text = { Text(stringResource(R.string.delete_task)) }, onClick = { deleteTask(task) ; onExpandChange(false) } )
    }
}