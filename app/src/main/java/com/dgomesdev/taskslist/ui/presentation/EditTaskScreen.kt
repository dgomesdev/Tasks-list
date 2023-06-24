package com.dgomesdev.taskslist.ui.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.TaskEntity

@OptIn(ExperimentalMaterial3Api::class)
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
    Surface {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = taskNameText,
                onValueChange = { taskNameText = it },
                label = { Text(stringResource(R.string.task_s_name)) },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            TextField(
                value = taskDescriptionText,
                onValueChange = { taskDescriptionText = it },
                label = { Text(stringResource(R.string.task_s_description)) },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            Button(
                onClick = { goToScreen("Main screen") },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Red)

            ) {
                Text(stringResource(R.string.cancel))
            }
            Button(
                onClick = { editTask(task.copy(name = taskNameText.text, description = taskDescriptionText.text)) ; goToScreen("Main screen")},
                modifier = Modifier.padding(8.dp)
            ) {
                Text(stringResource(R.string.confirm_edition))
            }
        }
    }
}