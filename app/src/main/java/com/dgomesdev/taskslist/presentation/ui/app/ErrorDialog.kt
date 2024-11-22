package com.dgomesdev.taskslist.presentation.ui.app

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun ErrorDialog(
    error: String,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            Button(onClick = { onDismissRequest() }) {
                Text("OK")
            }
        },
        icon = {
            Icon(imageVector = Icons.Default.Warning, contentDescription = "Warning logo")
        },
        text = {
            Text(
                error,
                Modifier.fillMaxWidth(),
                fontSize = 20.sp,
                textAlign = TextAlign.Center)
        }

    )
}

@Preview
@Composable
private fun DialogPreview() {
    ErrorDialog(
        "Task name cannot be empty"
    ) {}
}