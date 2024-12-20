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
import com.dgomesdev.taskslist.domain.model.Priority

@Composable
fun PrioritySetter(
    modifier: Modifier = Modifier,
    setPriority: (Priority) -> Unit,
    currentPriority: Priority
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Button(
        onClick = { expandedMenu = true },
        modifier = modifier
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (currentPriority) {
                    Priority.LOW -> stringResource(R.string.low)
                    Priority.MEDIUM -> stringResource(R.string.medium)
                    Priority.HIGH -> stringResource(R.string.high)
                }
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.options)
            )
        }
        DropdownMenu(expanded = expandedMenu, onDismissRequest = { expandedMenu = false }) {
            Priority.entries.forEach { priority ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = when (priority) {
                                Priority.LOW -> stringResource(R.string.low)
                                Priority.MEDIUM -> stringResource(R.string.medium)
                                Priority.HIGH -> stringResource(R.string.high)
                            }
                        )
                    },
                    onClick = { setPriority(priority); expandedMenu = false }
                )
            }
        }
    }
}

@Preview
@Composable
fun PrioritySetterPreview() {
    PrioritySetter(setPriority = {}, currentPriority = Priority.LOW)
}