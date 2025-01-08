package com.dgomesdev.taskslist.presentation.ui.features.resetPassword


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.presentation.ui.app.EMAIL_PATTERN
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
    val (recoveryCode, setRecoveryCode) = rememberSaveable { mutableStateOf(uiState.recoveryCode ?: "") }
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    val (newPassword, setNewPassword) = rememberSaveable { mutableStateOf("") }
    val (confirmPassword, setConfirmPassword) = rememberSaveable { mutableStateOf("") }
    val (isPasswordShown, setIsPasswordShown) = remember { mutableStateOf(false) }
    val (isConfirmPasswordShown, setIsConfirmPasswordShown) = remember { mutableStateOf(false) }
    val (isEmailValid, setIsEmailValid) = rememberSaveable { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current

    val isUserValid = recoveryCode.isNotBlank()
            && email.isNotBlank()
            && isEmailValid
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
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.update_password),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = recoveryCode,
                onValueChange = { setRecoveryCode(it) },
                label = { Text(stringResource(R.string.recovery_code)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { input ->
                    setEmail(input)
                    setIsEmailValid(input.matches(EMAIL_PATTERN.toRegex()))
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.email)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = stringResource(R.string.email)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                }),
                isError = !isEmailValid,
                singleLine = true
            )

            OutlinedTextField(
                value = newPassword,
                onValueChange = { setNewPassword(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.password)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = stringResource(R.string.password)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { setIsPasswordShown(!isPasswordShown) }) {
                        Icon(
                            if (isPasswordShown) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility,
                            contentDescription = if (isPasswordShown) stringResource(R.string.hide_password)
                            else stringResource(R.string.show_password)
                        )
                    }
                },
                visualTransformation =
                if (isPasswordShown) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                }),
                singleLine = true
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { setConfirmPassword(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.confirm_password)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = stringResource(R.string.password)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { setIsConfirmPasswordShown(!isConfirmPasswordShown) }) {
                        Icon(
                            if (isConfirmPasswordShown) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility,
                            contentDescription = if (isConfirmPasswordShown) stringResource(R.string.hide_password)
                            else stringResource(R.string.show_password)
                        )
                    }
                },
                visualTransformation =
                if (isConfirmPasswordShown) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onDone = { if (isUserValid) {
                        onAction(
                            ResetPassword(
                                recoveryCode,
                                User(
                                    email = email,
                                    password = confirmPassword
                                )
                            )
                        )
                        backToMainScreen()
                    }
                        else focusManager.clearFocus() }
                ),
                isError = newPassword != confirmPassword,
                singleLine = true
            )

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
                        if (isUserValid) onAction(
                            ResetPassword(
                                recoveryCode,
                                User(
                                    email = email,
                                    password = newPassword
                                )
                            )
                        )
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