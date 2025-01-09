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
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.Priority.HIGH
import com.dgomesdev.taskslist.domain.model.Priority.LOW
import com.dgomesdev.taskslist.domain.model.Priority.MEDIUM
import com.dgomesdev.taskslist.domain.model.Status.DONE
import com.dgomesdev.taskslist.domain.model.Status.IN_PROGRESS
import com.dgomesdev.taskslist.domain.model.Status.TO_BE_DONE
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.presentation.ui.app.ChooseTask
import com.dgomesdev.taskslist.presentation.ui.app.NewTaskButton
import com.dgomesdev.taskslist.presentation.ui.app.OnAction
import com.dgomesdev.taskslist.presentation.ui.app.ScreenNavigation
import com.dgomesdev.taskslist.presentation.ui.app.TaskAppBar
import com.dgomesdev.taskslist.presentation.ui.features.auth.AccountManager
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.DeleteTask
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.SetHasGoogleCredential
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.ShowSnackbar
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState
import com.dgomesdev.taskslist.utils.SortOption
import com.dgomesdev.taskslist.utils.TaskFilter
import com.dgomesdev.taskslist.utils.filterTasks
import com.dgomesdev.taskslist.utils.sortTasks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun TaskList(
    modifier: Modifier,
    uiState: AppUiState,
    onAction: OnAction,
    goToScreen: ScreenNavigation,
    onChooseTask: ChooseTask,
    scope: CoroutineScope,
    drawerState: DrawerState,
    accountManager: AccountManager
) {
    val taskList = uiState.user?.tasks ?: emptyList()
    val username = uiState.user?.username ?: ""
    val (currentSortOption, setCurrentSortOption) = rememberSaveable { mutableStateOf(SortOption.BY_TITLE) }
    val (selectedPriorities, setSelectedPriorities) = rememberSaveable {
        mutableStateOf(
            listOf(
                LOW,
                MEDIUM,
                HIGH
            )
        )
    }
    val (selectedStatuses, setSelectedStatuses) = rememberSaveable {
        mutableStateOf(
            listOf(
                TO_BE_DONE,
                IN_PROGRESS,
                DONE
            )
        )
    }
    val filteredAndSortedTasks = taskList
        .filterTasks(TaskFilter(selectedPriorities.toSet(), selectedStatuses.toSet()))
        .sortTasks(currentSortOption)
    val developerMessage = stringResource(R.string.developed_by_dgomes_dev)

    Scaffold(
        topBar = {
            TaskAppBar(
                scope = scope,
                drawerState = drawerState,
                onShowInfo = { onAction(ShowSnackbar(developerMessage)) }
            )
        },
        floatingActionButton = { NewTaskButton(goToScreen = goToScreen, onChooseTask) },
        snackbarHost = { SnackbarHost(uiState.snackbarHostState) }
    ) { padding ->
        LaunchedEffect(uiState.message) {
            uiState.message?.let {
                ShowSnackbar(it)
            }
            if (!uiState.hasGoogleCredential && uiState.email.isNotBlank() && uiState.password.isNotBlank()) {
                scope.launch {
                        uiState.user?.let { accountManager.createCredential(User(
                            uiState.email,
                            uiState.password
                        )) }
                        SetHasGoogleCredential(true)
                }
            }
        }
        Column(
            modifier.padding(padding)
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    stringResource(R.string.hello, username),
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
                                setCurrentSortOption(it)
                            }
                        )
                        FilterMenu(
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(8.dp),
                            selectedPriorities = selectedPriorities,
                            onPrioritiesChange = {
                                setSelectedPriorities(it)
                            },
                            selectedStatuses = selectedStatuses,
                            onStatusesChange = {
                                setSelectedStatuses(it)
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
                                    onAction(DeleteTask(task))
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
    onChooseTask: ChooseTask
) {

    val haptics = LocalHapticFeedback.current
    val (isCardExpanded, setCardExpanded) = remember { mutableStateOf(false) }
    val (areOptionsExpanded, setAreOptionsExpanded) = remember { mutableStateOf(false) }

    val status = when (task.status) {
        TO_BE_DONE -> stringResource(R.string.to_do)
        DONE -> stringResource(R.string.done)
        IN_PROGRESS -> stringResource(R.string.in_progress)
    }

    val priority = when (task.priority) {
        LOW -> stringResource(R.string.low)
        MEDIUM -> stringResource(R.string.medium)
        HIGH -> stringResource(R.string.high)
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
                onClick = { setCardExpanded(isCardExpanded) },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    setAreOptionsExpanded(true)
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
                onOpenOptions = { setAreOptionsExpanded(!areOptionsExpanded) },
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
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.status) + ":")
                Text(status)
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.priority_level) + ":")
                Text(priority)
            }
        }
        if (isCardExpanded) {
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
                onClick = { setCardExpanded(!isCardExpanded) }
            ) {
                Icon(
                    imageVector = if (isCardExpanded) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (isCardExpanded) stringResource(R.string.show_less)
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
            MEDIUM,
            TO_BE_DONE
        ),
        goToScreen = {},
        onAction = {},
        onChooseTask = {}
    )
}