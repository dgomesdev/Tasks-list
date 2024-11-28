package com.dgomesdev.taskslist.presentation.ui.features.taskDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.Status

@Composable
fun StatusSetter(
    modifier: Modifier = Modifier,
    setStatus: (Status) -> Unit,
    status: Status
) {
    var expandedMenu by remember { mutableStateOf(false) }

    var statusText = when (status) {
        Status.TO_BE_DONE -> stringResource(R.string.to_do)
        Status.IN_PROGRESS -> stringResource(R.string.in_progress)
        Status.DONE  -> stringResource(R.string.done)
    }

    Button(
        onClick = { expandedMenu = true },
        modifier = modifier
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(statusText)
            DropdownMenu(expanded = expandedMenu, onDismissRequest = { expandedMenu = false }) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.to_do)) },
                    onClick = { setStatus(Status.TO_BE_DONE); expandedMenu = false }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.in_progress)) },
                    onClick = { setStatus(Status.IN_PROGRESS); expandedMenu = false }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.done)) },
                    onClick = { setStatus(Status.DONE); expandedMenu = false }
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.options)
            )
        }
    }
}

@Preview
@Composable
fun StatusSetterPreview() {
    StatusSetter(setStatus = {}, status = Status.IN_PROGRESS)
}