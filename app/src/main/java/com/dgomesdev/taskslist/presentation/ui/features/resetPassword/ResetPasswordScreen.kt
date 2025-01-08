package com.dgomesdev.taskslist.presentation.ui.features.resetPassword


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.presentation.ui.app.OnAction
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.RefreshMessage
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.ResetPassword
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.ShowSnackbar
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState

@Composable
fun ResetPasswordScreen(
    modifier: Modifier,
    uiState: AppUiState,
    onAction: OnAction,
    backToMainScreen: () -> Unit,
) {
    var recoveryCode by rememberSaveable { mutableStateOf(uiState.recoveryCode ?: "") }
    var email by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isUpdating by remember { mutableStateOf(false) }

    val isUserValid = recoveryCode.isNotBlank()
            && email.isNotBlank()
            && newPassword.isNotBlank()
            && newPassword == confirmPassword

    Scaffold(
        snackbarHost = { SnackbarHost(uiState.snackbarHostState) }
    ) { padding ->

        LaunchedEffect(uiState.message) {
            uiState.message?.let {
                if (!it.contains("403")) ShowSnackbar(it)
                onAction(RefreshMessage)
            }
        }

        Column(
            modifier = modifier.padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.update_password),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = recoveryCode,
                onValueChange = { recoveryCode = it },
                label = { Text(stringResource(R.string.recovery_code)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text(stringResource(R.string.password)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.VisibilityOff,
                            contentDescription = if (showPassword) stringResource(R.string.hide_password) else stringResource(
                                R.string.show_password
                            )
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(R.string.confirm_password)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.VisibilityOff,
                            contentDescription = if (showPassword) stringResource(R.string.hide_password) else stringResource(
                                R.string.show_password
                            )
                        )
                    }
                },
                isError = newPassword != confirmPassword
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isUpdating) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { backToMainScreen() },
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Red
                    )
                ) {
                    Text(stringResource(R.string.cancel))
                }
                Button(
                    onClick = {
                        isUpdating = true
                        if (isUserValid) onAction(
                            ResetPassword(
                                recoveryCode,
                                User(
                                    email = email,
                                    password = newPassword
                                )
                            )
                        )
                        isUpdating = false
                        backToMainScreen()
                    },
                    modifier = Modifier.padding(8.dp),
                    enabled = isUserValid
                ) {
                    Text(text = stringResource(R.string.update))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UpdatePreview() {
    ResetPasswordScreen(
        modifier = Modifier.fillMaxSize(),
        uiState = AppUiState(),
        onAction = {},
        backToMainScreen = {},
    )
}