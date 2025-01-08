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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.Priority
import com.dgomesdev.taskslist.domain.model.Priority.HIGH
import com.dgomesdev.taskslist.domain.model.Priority.LOW
import com.dgomesdev.taskslist.domain.model.Priority.MEDIUM

@Composable
fun PrioritySetter(
    modifier: Modifier = Modifier,
    setPriority: (Priority) -> Unit,
    currentPriority: Priority
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
            Text(
                text = when (currentPriority) {
                    LOW -> stringResource(R.string.low)
                    MEDIUM -> stringResource(R.string.medium)
                    HIGH -> stringResource(R.string.high)
                }
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.options)
            )
        }
        DropdownMenu(expanded = expandedMenu, onDismissRequest = { setExpandedMenu(false) }) {
            Priority.entries.forEach { priority ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = when (priority) {
                                LOW -> stringResource(R.string.low)
                                MEDIUM -> stringResource(R.string.medium)
                                HIGH -> stringResource(R.string.high)
                            }
                        )
                    },
                    onClick = { setPriority(priority); setExpandedMenu(false) }
                )
            }
        }
    }
}

@Preview
@Composable
fun PrioritySetterPreview() {
    PrioritySetter(setPriority = {}, currentPriority = LOW)
}