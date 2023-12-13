package com.dgomesdev.taskslist.ui.presentation.screens

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.domain.TaskEntity
import com.dgomesdev.taskslist.ui.composables.DateSetter
import com.dgomesdev.taskslist.ui.composables.PrioritySetter
import com.dgomesdev.taskslist.ui.presentation.EditTask
import com.dgomesdev.taskslist.ui.routes.ScreenNavigation
import com.dgomesdev.taskslist.utils.validateEndDate

@Composable
fun EditTaskScreen(
    editTask: EditTask,
    task: TaskEntity,
    goToScreen: ScreenNavigation
) {
    var taskNameText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(task.name))
    }
    var taskDescriptionText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(task.description))
    }
    var taskStartDate by rememberSaveable { mutableStateOf(task.startDate) }
    var taskEndDate by rememberSaveable { mutableStateOf(task.endDate) }
    var taskPriority by rememberSaveable { mutableIntStateOf(task.priority) }

    var isEndDateValid by remember { mutableStateOf(true) }

    Surface {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = taskNameText,
                onValueChange = { taskNameText = it },
                label = { Text("Task's name") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            TextField(
                value = taskDescriptionText,
                onValueChange = { taskDescriptionText = it },
                label = { Text("Task's description") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            Row(Modifier.padding(8.dp)) {
                DateSetter(
                    Modifier.weight(0.5f),
                    "$taskStartDate",
                    selectedDate = { taskStartDate = it },
                    isEndDateValid = isEndDateValid
                )
                DateSetter(
                    Modifier.weight(0.5f),
                    "$taskEndDate",
                    selectedDate = { taskEndDate = it },
                    isEndDateValid = isEndDateValid
                )
            }
            PrioritySetter { taskPriority = it }
            Button(
                onClick = { goToScreen("Main screen") },
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
                    isEndDateValid = validateEndDate(taskStartDate, taskEndDate)
                    if (isEndDateValid) {
                        editTask(
                            task.copy(
                                name = taskNameText.text,
                                description = taskDescriptionText.text,
                                startDate = taskStartDate,
                                endDate = taskEndDate,
                                priority = taskPriority
                            )
                        )
                        goToScreen("Main screen")
                    }
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
private fun EditTaskPreview() {
    EditTaskScreen(
        editTask = {},
        task = TaskEntity(
            name = "Task example",
            description = "This is an example of task",
            priority = 2,
            startDate = null,
            endDate = null
        ),
        goToScreen = {}
    )
}