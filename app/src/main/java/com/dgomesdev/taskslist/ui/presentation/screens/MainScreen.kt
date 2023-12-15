package com.dgomesdev.taskslist.ui.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dgomesdev.taskslist.domain.TaskEntity
import com.dgomesdev.taskslist.ui.composables.TaskList
import com.dgomesdev.taskslist.ui.composables.DeleteTask
import com.dgomesdev.taskslist.ui.composables.SetStatus
import com.dgomesdev.taskslist.ui.routes.ScreenNavigation

@Composable
fun MainScreen(
    tasks: List<TaskEntity>,
    deleteTask: DeleteTask,
    screenNavigation: ScreenNavigation,
    modifier: Modifier,
    setStatus: SetStatus
) {
    Column(
        modifier = modifier
    ) {
        TaskList(
            taskList = tasks,
            modifier = modifier,
            deleteTask = deleteTask,
            screenNavigation = screenNavigation,
            setStatus = setStatus
        )
    }
}