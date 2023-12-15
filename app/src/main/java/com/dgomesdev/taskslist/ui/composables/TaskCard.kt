package com.dgomesdev.taskslist.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgomesdev.taskslist.domain.Status
import com.dgomesdev.taskslist.domain.TaskEntity
import com.dgomesdev.taskslist.ui.routes.ScreenNavigation
import com.dgomesdev.taskslist.ui.theme.AlmostLateColor
import com.dgomesdev.taskslist.ui.theme.DoneColor
import com.dgomesdev.taskslist.ui.theme.LateColor
import com.dgomesdev.taskslist.ui.theme.ToDoColor

@Composable
fun TaskCard(
    task: TaskEntity,
    deleteTask: DeleteTask,
    goToScreen: ScreenNavigation,
    setStatus: EditTask
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var expandedMenu by remember {
        mutableStateOf(false)
    }
    val status = when (task.status) {
        Status.TO_DO -> "To do"
        Status.DONE -> "Done"
        Status.ALMOST_LATE -> "Almost late"
        Status.LATE -> "Late"
    }
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                Modifier
                    .weight(0.15f)
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    when (status) {
                        "Done" -> DoneColor
                        "Almost late" -> AlmostLateColor
                        "Late" -> LateColor
                        else -> ToDoColor
                    }
                )
            ) {
                Text(
                    text = "${task.priority}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                    )
            }
            Text(
                text = task.name,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.65f),
                style = TextStyle(
                    textDecoration =
                    if (task.isTaskDone) TextDecoration.LineThrough
                    else TextDecoration.None
                )
            )
            IconButton(
                onClick = { expanded = !expanded },
                Modifier.weight(0.1f)
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "show less"
                    else "show more"
                )
            }
            IconButton(
                onClick = { expandedMenu = true },
                Modifier.weight(0.1f)
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Task options")
                TaskOptions(
                    expandedMenu = expandedMenu,
                    onExpandChange = { expandedMenu = it },
                    deleteTask = deleteTask,
                    task = task,
                    goToScreen = goToScreen,
                    isTaskDone = task.isTaskDone,
                    onMarkAsDone = {
                        setStatus(task.copy(isTaskDone = !task.isTaskDone))
                    }
                )
            }
        }
        if (expanded) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Status: $status",
                    Modifier.weight(0.5f)
                )
                Text(
                    "Priority level: ${task.priority}",
                    Modifier.weight(0.5f),
                    textAlign = TextAlign.End
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (task.startDate != null) Text(
                    "Start date: ${task.startDate}",
                    Modifier.weight(0.5f)
                )
                if (task.endDate != null) Text(
                    "End date: ${task.endDate}",
                    Modifier.weight(0.5f),
                    textAlign = TextAlign.End
                )
            }
            Row(
                Modifier.padding(8.dp)
            ) {
                if (task.description.isNotBlank()) Text(
                    task.description,
                    Modifier.padding(8.dp),
                    textAlign = TextAlign.Justify
                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskCardPreview() {
    TaskCard(
        TaskEntity(
            0,
            "Task name",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor" +
                    " incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis " +
                    "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                    "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
                    "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in " +
                    "culpa qui officia deserunt mollit anim id est laborum.",
            Status.ALMOST_LATE,
            null,
            "09/07/21",
            2
        ),
        {},
        {},
        {}
    )
}