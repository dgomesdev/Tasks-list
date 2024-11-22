package com.dgomesdev.taskslist.presentation.ui.features.taskDetails

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.Status

@Composable
fun StatusSetter(
    setStatus: (Status) -> Unit,
    status: Status
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Button (
        modifier = Modifier.width(168.dp),
        onClick = { expandedMenu = true }
    ) {
        Text(status.name)
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = stringResource(R.string.task_options)
        )
        DropdownMenu(expanded = expandedMenu, onDismissRequest = { expandedMenu = false }) {
            DropdownMenuItem(
                text = { Text("To do") },
                onClick = { setStatus(Status.TO_BE_DONE); expandedMenu = false }
            )
            DropdownMenuItem(
                text = { Text("In progress") },
                onClick = { setStatus(Status.IN_PROGRESS); expandedMenu = false }
            )
            DropdownMenuItem(
                text = { Text("Done") },
                onClick = { setStatus(Status.DONE); expandedMenu = false }
            )
        }
    }
}

@Preview
@Composable
fun StatusSetterPreview() {
    StatusSetter(setStatus = {}, status = Status.IN_PROGRESS)
}