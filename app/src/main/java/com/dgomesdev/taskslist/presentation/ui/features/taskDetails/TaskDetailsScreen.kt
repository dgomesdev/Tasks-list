package com.dgomesdev.taskslist.presentation.ui.features.taskDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.Priority
import com.dgomesdev.taskslist.domain.model.Status
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.presentation.ui.app.OnAction
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent
import java.util.UUID

@Composable
fun TaskDetailsScreen(
    modifier: Modifier,
    task: Task?,
    onAction: OnAction,
    backToMainScreen: () -> Unit = {}
) {
    val (title, setTitle) = rememberSaveable { mutableStateOf(task?.title ?: "") }
    val (description, setDescription) = rememberSaveable { mutableStateOf(task?.description ?: "") }
    val (priority, setPriority) = rememberSaveable { mutableStateOf(task?.priority ?: Priority.MEDIUM) }
    val (status, setStatus) = rememberSaveable { mutableStateOf(task?.status ?: Status.TO_BE_DONE) }
    val focusManager = LocalFocusManager.current

    Surface {
        Column(
            modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { setTitle(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.title)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                })
            )

            OutlinedTextField(
                value = description,
                onValueChange = { setDescription(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.description)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() })
            )

            Row(
                Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(stringResource(R.string.priority_level))
                Text(stringResource(R.string.status))
            }

            Row(
                Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                PrioritySetter(
                    Modifier.weight(0.5f).padding(horizontal = 8.dp),
                    setPriority = { setPriority(it) },
                    currentPriority = priority
                )
                StatusSetter(
                    Modifier.weight(0.5f).padding(horizontal = 8.dp),
                    setStatus = { setStatus(it) },
                    currentStatus = status
                )
            }

            Button(
                onClick = { backToMainScreen() },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Red
                )
            ) {
                Text(stringResource(R.string.cancel))
            }

            Button(
                onClick = {
                    if (task == null) onAction(
                        AppUiIntent.SaveTask(
                            Task(
                                title = title,
                                description = description,
                                priority = priority,
                                status = status
                            )
                        )
                    ) else {
                        onAction(
                            AppUiIntent.UpdateTask(
                                task.copy(
                                    title = title,
                                    description = description,
                                    priority = priority,
                                    status = status
                                )
                            )
                        )
                    }
                    backToMainScreen()
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    }
}

@Preview
@Composable
private fun TaskListPreview() {
    TaskDetailsScreen(
        modifier = Modifier.fillMaxSize(),
        onAction = {},
        task = Task(
            UUID.randomUUID().toString(),
            "Task title",
            "Task description",
            Priority.LOW,
            Status.IN_PROGRESS
        ),
        backToMainScreen = {}
    )
}