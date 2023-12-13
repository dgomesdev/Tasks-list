package com.dgomesdev.taskslist.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.utils.getDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSetter(
    modifier: Modifier,
    labelTest: String,
    selectedDate: (String) -> Unit,
    isEndDateValid: Boolean
) {

    var isDatePickerDialogShown by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf("") }
    val datePickerState = rememberDatePickerState()
    val focusManager = LocalFocusManager.current

    if (isDatePickerDialogShown) {
        DatePickerDialog(
            onDismissRequest = { isDatePickerDialogShown = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            date = millis.getDate()
                        }
                        selectedDate(date)
                        isDatePickerDialogShown = false
                    }
                ) {
                    Text("Set date")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    TextField(
        value = date,
        onValueChange = {},
        modifier
            .padding(8.dp)
            .onFocusChanged {
                if (it.isFocused) {
                    isDatePickerDialogShown = true
                    focusManager.clearFocus(force = true)
                }
            },
        readOnly = true,
        label = {
            Text(labelTest)
        },
        isError = !isEndDateValid
    )
}