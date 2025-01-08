package com.dgomesdev.taskslist.presentation.ui.features.taskList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import com.dgomesdev.taskslist.domain.model.Priority
import com.dgomesdev.taskslist.domain.model.Status

@Composable
fun FilterMenu(
    modifier: Modifier = Modifier,
    selectedPriorities: List<Priority> = emptyList(),
    onPrioritiesChange: (List<Priority>) -> Unit = {},
    selectedStatuses: List<Status> = emptyList(),
    onStatusesChange: (List<Status>) -> Unit = {}
) {
    val (expandedMenu, setExpandedMenu) = remember { mutableStateOf(false) }

    Button(
        onClick = { setExpandedMenu(true) },
        modifier = modifier
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.filter_by))
            Icon(
                imageVector = Icons.Default.FilterAlt,
                contentDescription = stringResource(R.string.filter_by)
            )
        }

        DropdownMenu(
            expanded = expandedMenu,
            onDismissRequest = { setExpandedMenu(false) }
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = stringResource(R.string.priority))
            }
            Priority.entries.forEach { priority ->
                val isSelected = priority in selectedPriorities
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text(
                        text = when (priority) {
                            Priority.LOW -> stringResource(R.string.low)
                            Priority.MEDIUM -> stringResource(R.string.medium)
                            Priority.HIGH -> stringResource(R.string.high)
                        }
                    ) },
                    onClick = {
                        val updatedPriorities = if (isSelected) {
                            selectedPriorities - priority
                        } else {
                            selectedPriorities + priority
                        }
                        onPrioritiesChange(updatedPriorities)
                    },
                    trailingIcon = {
                        if (isSelected) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = stringResource(R.string.selected)
                            )
                        }
                    }
                )
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = stringResource(R.string.status))
            }
            Status.entries.forEach { status ->
                val isSelected = status in selectedStatuses
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text(
                        text = when (status) {
                            Status.TO_BE_DONE -> stringResource(R.string.to_do)
                            Status.IN_PROGRESS -> stringResource(R.string.in_progress)
                            Status.DONE -> stringResource(R.string.done)
                        }
                    ) },
                    onClick = {
                        val updatedStatuses = if (isSelected) {
                            selectedStatuses - status
                        } else {
                            selectedStatuses + status
                        }
                        onStatusesChange(updatedStatuses)
                    },
                    trailingIcon = {
                        if (isSelected) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = stringResource(R.string.selected)
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun FilterMenuPreview() {
    var selectedPriorities by remember { mutableStateOf(listOf<Priority>()) }
    var selectedStatuses by remember { mutableStateOf(listOf<Status>()) }

    FilterMenu(
        selectedPriorities = selectedPriorities,
        onPrioritiesChange = { selectedPriorities = it },
        selectedStatuses = selectedStatuses,
        onStatusesChange = { selectedStatuses = it }
    )
}
