package com.dgomesdev.taskslist.ui.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.dgomesdev.taskslist.ui.composables.ErrorDialog
import com.dgomesdev.taskslist.ui.composables.PrioritySetter
import com.dgomesdev.taskslist.ui.presentation.AddTask
import com.dgomesdev.taskslist.ui.presentation.RefreshList
import com.dgomesdev.taskslist.ui.routes.ScreenNavigation
import com.dgomesdev.taskslist.utils.validateEndDate
import com.dgomesdev.taskslist.utils.validateTaskName

@Composable
fun AddTaskScreen(
    addTask: AddTask,
    refreshList: RefreshList,
    goToScreen: ScreenNavigation
) {
    var taskName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var taskDescription by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var taskStartDate by rememberSaveable { mutableStateOf<String?>(null) }
    var taskEndDate by rememberSaveable { mutableStateOf<String?>(null) }
    var taskPriority by rememberSaveable { mutableIntStateOf(1) }

    var isTaskNameValid by remember { mutableStateOf(true) }
    var isEndDateValid by remember { mutableStateOf(true) }
    var error by rememberSaveable { mutableStateOf("") }
    var isErrorDialogOpen by remember {
        mutableStateOf(false)
    }

    val task = TaskEntity(
        name = taskName.text,
        description = taskDescription.text,
        startDate = taskStartDate,
        endDate = taskEndDate,
        priority = taskPriority
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isErrorDialogOpen) {
            ErrorDialog(
                error = error,
                onDismissRequest = { isErrorDialogOpen = false }
            )
        }
        TextField(
            value = taskName,
            onValueChange = { taskName = it },
            label = { Text("Task's name") },
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
        Row(Modifier.padding(8.dp)) {
            DateSetter(
                Modifier.weight(0.5f),
                "Set the start date",
                selectedDate = { taskStartDate = it },
                isEndDateValid = isEndDateValid
            )
            DateSetter(
                Modifier.weight(0.5f),
                "Set the end date",
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
                isTaskNameValid = validateTaskName(taskName.text)
                isEndDateValid = validateEndDate(taskStartDate, taskEndDate)
                if (isEndDateValid && isTaskNameValid) {
                    addTask(task)
                    refreshList()
                    goToScreen("Main screen")
                } else {
                    error = if (!isTaskNameValid) {
                        "Task name cannot be blank"
                    } else {
                        "Start date cannot be after end date"
                    }
                    isErrorDialogOpen = true
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Add task")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddTaskPreview() {
    AddTaskScreen(
        addTask = {},
        refreshList = {},
        goToScreen = {}
    )
}