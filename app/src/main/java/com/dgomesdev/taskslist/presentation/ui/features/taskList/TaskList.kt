package com.dgomesdev.taskslist.presentation.ui.features.taskList

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.Priority
import com.dgomesdev.taskslist.domain.model.Status
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.presentation.ui.app.ChooseTask
import com.dgomesdev.taskslist.presentation.ui.app.HandleTaskAction
import com.dgomesdev.taskslist.presentation.ui.app.NewTaskButton
import com.dgomesdev.taskslist.presentation.ui.app.ScreenNavigation
import com.dgomesdev.taskslist.presentation.ui.app.TaskAppBar
import com.dgomesdev.taskslist.presentation.ui.theme.AlmostLateColor
import com.dgomesdev.taskslist.presentation.ui.theme.DoneColor
import com.dgomesdev.taskslist.presentation.ui.theme.ToDoColor
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun TaskList(
    modifier: Modifier,
    uiState: AppUiState,
    handleTaskAction: HandleTaskAction,
    goToScreen: ScreenNavigation,
    onChooseTask: ChooseTask,
    onOpenDrawer: () -> Unit
) {
    val taskList = uiState.user?.tasks
    val message = uiState.message
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = modifier,
        topBar = { TaskAppBar(onOpenDrawer = onOpenDrawer) },
        floatingActionButton = {
            NewTaskButton(
                goToScreen = goToScreen
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LaunchedEffect(message) {
            if (message != null) {
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
        if (taskList != null) {
            LazyColumn(
                contentPadding = padding,
            ) {
                items(taskList) { task ->
                    TaskCard(
                        task = task,
                        handleTaskAction = handleTaskAction,
                        goToScreen = goToScreen,
                        onChooseTask = onChooseTask
                    )
                }
            }
        } else {
            Box(
                modifier = modifier.padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No tasks")
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    goToScreen: ScreenNavigation,
    handleTaskAction: HandleTaskAction,
    onChooseTask: ChooseTask = {}
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val status = when (task.status) {
        Status.TO_BE_DONE -> stringResource(R.string.to_do)
        Status.DONE -> stringResource(R.string.done)
        Status.IN_PROGRESS -> stringResource(R.string.almost_late)
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
                    .weight(0.3f)
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    when (task.status) {
                        Status.DONE -> DoneColor
                        Status.IN_PROGRESS -> AlmostLateColor
                        Status.TO_BE_DONE -> ToDoColor
                    }
                )
            ) {
                Text(
                    text = "${task.priority}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = task.title,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.7f)
            )
            IconButton(
                onClick = { expanded = !expanded },
                Modifier.weight(0.1f)
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) stringResource(R.string.show_less)
                    else stringResource(R.string.show_more)
                )
            }
            TaskOptions(
                handleTaskAction = handleTaskAction,
                task = task,
                goToScreen = goToScreen,
                onChooseTask = onChooseTask
            )
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
                    stringResource(R.string.priority_level) + ": ${task.priority}",
                    Modifier.weight(0.5f),
                    textAlign = TextAlign.End
                )
            }
            Row(
                Modifier.padding(8.dp)
            ) {
                if (task.description.isNotBlank()) {
                    Text(
                        task.description,
                        Modifier.padding(8.dp),
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TaskCardPreview() {
    TaskCard(
        task = Task(
            UUID.randomUUID().toString(),
            "title",
            "description",
            Priority.LOW,
            Status.TO_BE_DONE
        ),
        goToScreen = {},
        handleTaskAction = { _, _ -> }
    )
}