package com.dgomesdev.taskslist.presentation.ui.features.taskDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.domain.model.Priority
import com.dgomesdev.taskslist.domain.model.Status
import com.dgomesdev.taskslist.domain.model.Task
import com.dgomesdev.taskslist.domain.model.TaskAction
import com.dgomesdev.taskslist.presentation.ui.app.HandleTaskAction
import com.dgomesdev.taskslist.presentation.ui.app.ScreenNavigation
import java.util.UUID

@Composable
fun TaskDetailsScreen(
    handleTaskAction: HandleTaskAction,
    task: Task?,
    goToScreen: ScreenNavigation
) {
    var taskTitle by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(task?.title ?: ""))
    }

    var taskDescription by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(task?.description ?: ""))
    }

    var taskPriority by rememberSaveable {
        mutableStateOf(task?.priority ?: Priority.MEDIUM)
    }

    var taskStatus by rememberSaveable {
        mutableStateOf(task?.status ?: Status.TO_BE_DONE)
    }

    Surface {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                label = { Text("Task's title") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            TextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("Task's description") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                PrioritySetter(
                    setPriority = { taskPriority = it },
                    priority = taskPriority
                )
                StatusSetter(
                    setStatus = { taskStatus = it },
                    status = taskStatus
                )
            }
            Button(
                onClick = { goToScreen("TaskList") },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Red
                )
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    handleTaskAction(
                        if (task == null) TaskAction.SAVE else TaskAction.UPDATE,
                        Task(
                            task?.taskId,
                            taskTitle.text,
                            taskDescription.text,
                            taskPriority,
                            taskStatus
                        )
                    )
                    goToScreen("TaskList")
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Confirm Edition")
            }
        }
    }
}

@Preview
@Composable
private fun TaskListPreview() {
    TaskDetailsScreen(
        handleTaskAction = {_, _ ->},
        task = Task(UUID.randomUUID().toString(), "Task title", "Task description", Priority.LOW, Status.IN_PROGRESS),
        goToScreen = {}
    )
}