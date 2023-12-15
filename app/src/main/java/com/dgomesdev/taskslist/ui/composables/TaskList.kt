package com.dgomesdev.taskslist.ui.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dgomesdev.taskslist.domain.TaskEntity
import com.dgomesdev.taskslist.ui.routes.ScreenNavigation

@Composable
fun TaskList(
    taskList: List<TaskEntity>,
    deleteTask: DeleteTask,
    screenNavigation: ScreenNavigation,
    setStatus: EditTask
) {
    LazyColumn {
        items(taskList) { task ->
            TaskCard(
                task = task,
                deleteTask = deleteTask,
                goToScreen = screenNavigation,
                setStatus = setStatus
            )
        }
    }
}