package com.dgomesdev.taskslist.ui.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dgomesdev.taskslist.domain.TaskEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    addTask: AddTask,
    refreshList: RefreshList,
    goToScreen: ScreenNavigation
) {
    var taskNameText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var taskDescriptionText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val task = TaskEntity(name = taskNameText.text, description = taskDescriptionText.text)
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
            Button(
                onClick = { goToScreen("Main screen") },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Red)
                ) {
                Text("Cancel")
            }
            Button(
                onClick = { addTask(task); refreshList() ; goToScreen("Main screen") },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Add task")
            }
        }
    }
}