package com.dgomesdev.taskslist.presentation.ui.features.taskList

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.Priority
import com.dgomesdev.taskslist.domain.model.Status
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.presentation.ui.app.ChooseTask
import com.dgomesdev.taskslist.presentation.ui.app.NewTaskButton
import com.dgomesdev.taskslist.presentation.ui.app.OnAction
import com.dgomesdev.taskslist.presentation.ui.app.ScreenNavigation
import com.dgomesdev.taskslist.presentation.ui.app.TaskAppBar
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState
import com.dgomesdev.taskslist.utils.SortOption
import com.dgomesdev.taskslist.utils.TaskFilter
import com.dgomesdev.taskslist.utils.filterTasks
import com.dgomesdev.taskslist.utils.sortTasks
import java.util.UUID

@Composable
fun TaskList(
    modifier: Modifier,
    uiState: AppUiState,
    onAction: OnAction,
    goToScreen: ScreenNavigation,
    onChooseTask: ChooseTask,
    onOpenDrawer: () -> Unit,
    snackbarHostState: SnackbarHostState,
    showSnackbar: (String) -> Unit
) {
    val context = LocalContext.current
    val taskList = uiState.user!!.tasks

    var currentSortOption by rememberSaveable { mutableStateOf(SortOption.BY_TITLE) }
    var selectedPriorities by rememberSaveable {
        mutableStateOf(
            listOf(
                Priority.LOW,
                Priority.MEDIUM,
                Priority.HIGH
            )
        )
    }
    var selectedStatuses by rememberSaveable {
        mutableStateOf(
            listOf(
                Status.TO_BE_DONE,
                Status.IN_PROGRESS,
                Status.DONE
            )
        )
    }
    val filteredAndSortedTasks = uiState.user.tasks
        .filterTasks(TaskFilter(selectedPriorities.toSet(), selectedStatuses.toSet()))
        .sortTasks(currentSortOption)

    Scaffold(
        topBar = {
            TaskAppBar(onOpenDrawer = onOpenDrawer, onShowInfo = {
                showSnackbar(context.getString(R.string.developed_by_dgomes_dev))
            })
        },
        floatingActionButton = { NewTaskButton(goToScreen = goToScreen) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LaunchedEffect(uiState.message) {
            uiState.message?.let { showSnackbar(it) }
        }
        Column (
            modifier.padding(padding)
        ){
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    uiState.user.username,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 32.sp
                )
            }
            if (taskList.isNotEmpty()) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        SortingMenu(
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(8.dp),
                            onSortOptionSelected = {
                                currentSortOption = it
                            }
                        )
                        FilterMenu(
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(8.dp),
                            selectedPriorities = selectedPriorities,
                            onPrioritiesChange = {
                                selectedPriorities = it
                            },
                            selectedStatuses = selectedStatuses,
                            onStatusesChange = {
                                selectedStatuses = it
                            }
                        )
                    }
                    HorizontalDivider()
                    LazyColumn(modifier) {
                        items(items = filteredAndSortedTasks, key = { task ->
                            task.taskId
                        }) { task ->
                            val swipeToDismissState = rememberSwipeToDismissBoxState()
                            LaunchedEffect(swipeToDismissState.currentValue) {
                                if (swipeToDismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                                    onAction(AppUiIntent.DeleteTask(task))
                                }
                            }
                            SwipeToDismissBox(
                                state = swipeToDismissState,
                                backgroundContent = {
                                    if (swipeToDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                                        Card(Modifier.padding(8.dp)) {
                                            Box(
                                                modifier.background(Color.Red),
                                                contentAlignment = Alignment.CenterEnd
                                            ) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = stringResource(R.string.delete_task),
                                                    Modifier.padding(end = 16.dp)
                                                )
                                            }
                                        }
                                    }
                                },
                                enableDismissFromStartToEnd = false
                            ) {
                                TaskCard(
                                    task = task,
                                    goToScreen = goToScreen,
                                    onAction = onAction,
                                    onChooseTask = onChooseTask
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = modifier.padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.no_tasks_for_now))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(
    task: Task,
    goToScreen: ScreenNavigation,
    onAction: OnAction,
    onChooseTask: ChooseTask = {}
) {

    val haptics = LocalHapticFeedback.current

    var expanded by remember {
        mutableStateOf(false)
    }

    var areOptionsExpanded by remember {
        mutableStateOf(false)
    }

    val status = when (task.status) {
        Status.TO_BE_DONE -> stringResource(R.string.to_do)
        Status.DONE -> stringResource(R.string.done)
        Status.IN_PROGRESS -> stringResource(R.string.in_progress)
    }

    val priority = when (task.priority) {
        Priority.LOW -> stringResource(R.string.low)
        Priority.MEDIUM -> stringResource(R.string.medium)
        Priority.HIGH -> stringResource(R.string.high)
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .combinedClickable(
                onClick = { expanded = !expanded },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    areOptionsExpanded = true
                },
                onClickLabel = stringResource(R.string.options)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = task.title
            )
            TaskOptions(
                onAction = onAction,
                task = task,
                goToScreen = goToScreen,
                onChooseTask = onChooseTask,
                onOpenOptions = { areOptionsExpanded = !areOptionsExpanded },
                areOptionsExpanded = areOptionsExpanded
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Status: $status"
            )
            Text(
                stringResource(R.string.priority_level) + ": $priority",
                textAlign = TextAlign.End
            )
        }
        if (expanded) {
            Row(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                if (task.description.isNotBlank()) {
                    Text(
                        task.description,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            IconButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) stringResource(R.string.show_less)
                    else stringResource(R.string.show_more)
                )
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
        onAction = {}
    )
}

@Preview
@Composable
private fun ListPrev() {
    TaskList(
        modifier = Modifier.fillMaxSize(),
        uiState = AppUiState(
            user = User(
                username = "Danilo",
                tasks = listOf(
                    Task(
                        UUID.randomUUID().toString(),
                        "title",
                        "description",
                        Priority.LOW,
                        Status.TO_BE_DONE
                    )
                )
            )
        ),
        onAction = {},
        goToScreen = {},
        onChooseTask = {},
        onOpenDrawer = {},
        snackbarHostState = SnackbarHostState(),
        showSnackbar = {}
    )
}