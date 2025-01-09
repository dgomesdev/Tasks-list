package com.dgomesdev.taskslist.presentation.ui.features.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.presentation.ui.app.EMAIL_PATTERN
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.*
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    modifier: Modifier,
    uiState: AppUiState,
    onAction: (AppUiIntent) -> Unit,
    backToMainScreen: () -> Unit,
    accountManager: AccountManager,
    scope: CoroutineScope
) {
    val (username, setUsername) = rememberSaveable { mutableStateOf("") }
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    val (newPassword, setNewPassword) = rememberSaveable { mutableStateOf("") }
    val (confirmPassword, setConfirmPassword) = rememberSaveable { mutableStateOf("") }
    val (isPasswordShown, setIsPasswordShown) = remember { mutableStateOf(false) }
    val (isConfirmPasswordShown, setIsConfirmPasswordShown) = remember { mutableStateOf(false) }
    val (isEmailValid, setIsEmailValid) = rememberSaveable { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current

    val isUserValid =
        username.isNotBlank()
                && email.isNotBlank()
                && isEmailValid
                && newPassword.isNotBlank()
                && newPassword == confirmPassword

    fun signUp() {
        scope.launch {
            val result = accountManager.createCredential(
                User(
                    username = username,
                    email = email,
                    password = confirmPassword,
                )
            )
            onAction(Register(result))
        }
        backToMainScreen()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(uiState.snackbarHostState) }
    ) { padding ->

        val alreadyHaveAnAccountMessage = stringResource(R.string.already_have_an_account)

        LaunchedEffect(uiState.message) {
            uiState.message?.let {
                if (it.contains("406")) ShowSnackbar(alreadyHaveAnAccountMessage)
            }
        }

        Column(
            modifier = modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.sign_up),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { setUsername(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.username)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = stringResource(R.string.username)
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                }),
                singleLine = true
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
                    onDone = { if (isUserValid) signUp() else focusManager.clearFocus() }
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
                    onClick = { signUp() },
                    modifier = Modifier.padding(8.dp),
                    enabled = isUserValid
                ) {
                    Text(text = stringResource(R.string.sign_up))
                }

            TextButton(onClick = { backToMainScreen() }) {
                Text(
                    text = stringResource(R.string.already_have_an_account)
                )
            }
        }
    }
}