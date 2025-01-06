package com.dgomesdev.taskslist.presentation.ui.features.taskDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
    var taskTitle by rememberSaveable { mutableStateOf(task?.title ?: "") }

    var taskDescription by rememberSaveable { mutableStateOf(task?.description ?: "") }

    var taskPriority by rememberSaveable { mutableStateOf(task?.priority ?: Priority.MEDIUM) }

    var taskStatus by rememberSaveable { mutableStateOf(task?.status ?: Status.TO_BE_DONE) }

    Surface {
        Column(
            modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                label = { Text(stringResource(R.string.title)) },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            OutlinedTextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text(stringResource(R.string.description)) },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                PrioritySetter(
                    Modifier
                        .weight(0.5f)
                        .padding(horizontal = 8.dp),
                    setPriority = { taskPriority = it },
                    currentPriority = taskPriority
                )
                StatusSetter(
                    Modifier
                        .weight(0.5f)
                        .padding(horizontal = 8.dp),
                    setStatus = { taskStatus = it },
                    currentStatus = taskStatus
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
                                title = taskTitle,
                                description = taskDescription,
                                priority = taskPriority,
                                status = taskStatus
                            )
                        )
                    ) else {
                        onAction(
                            AppUiIntent.UpdateTask(
                                task.copy(
                                    title = taskTitle,
                                    description = taskDescription,
                                    priority = taskPriority,
                                    status = taskStatus
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