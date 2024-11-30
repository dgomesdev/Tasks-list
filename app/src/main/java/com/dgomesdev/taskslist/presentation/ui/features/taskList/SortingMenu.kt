package com.dgomesdev.taskslist.presentation.ui.features.taskList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
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
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.utils.SortOption

@Composable
fun SortingMenu(
    modifier: Modifier,
    onSortOptionSelected: (SortOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Button(
        onClick = { expanded = true },
        modifier = modifier
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.sort_by))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = stringResource(R.string.sort_by)
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            SortOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = when (option) {
                                SortOption.BY_TITLE -> stringResource(R.string.title)
                                SortOption.BY_PRIORITY -> stringResource(R.string.priority)
                                SortOption.BY_STATUS -> stringResource(R.string.status)
                            }
                        )
                    },
                    onClick = { onSortOptionSelected(option); expanded = false }
                )
            }
        }
    }
}