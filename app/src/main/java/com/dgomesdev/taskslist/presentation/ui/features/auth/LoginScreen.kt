package com.dgomesdev.taskslist.presentation.ui.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import com.dgomesdev.taskslist.presentation.ui.app.MainActivity
import com.dgomesdev.taskslist.presentation.ui.app.OnAction
import com.dgomesdev.taskslist.presentation.ui.features.auth.GetCredentialResult.Success
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.Login
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.SetEmail
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.SetHasGoogleCredential
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.SetIsSessionValid
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.SetPassword
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.ShowSnackbar
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    modifier: Modifier,
    uiState: AppUiState,
    onAction: OnAction,
    goToScreen: (String) -> Unit,
    accountManager: AccountManager,
    scope: CoroutineScope
) {
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val (isPasswordShown, setIsPasswordShown) = rememberSaveable { mutableStateOf(false) }
    val (isEmailValid, setIsEmailValid) = rememberSaveable { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    val alreadyHaveAnAccountMessage = stringResource(R.string.already_have_an_account)

    Scaffold(
        snackbarHost = { SnackbarHost(uiState.snackbarHostState) }
    ) { padding ->

        LaunchedEffect(uiState) {
            if (!uiState.isSessionValid) {
                scope.launch {
                    when (val result = accountManager.getCredential()) {
                        is Success -> {
                            onAction(SetEmail(result.user.email))
                            onAction(SetPassword(result.user.password))
                            onAction(SetHasGoogleCredential(true))
                            onAction(Login(result.user))
                        }

                        is GetCredentialResult.Cancelled -> {
                            onAction(SetHasGoogleCredential(true))
                            onAction(SetIsSessionValid(true))
                        }

                        else -> onAction(SetHasGoogleCredential(false))
                    }
                }
            }
            uiState.message?.let {
                when {
                    it.contains("409") -> onAction(ShowSnackbar(alreadyHaveAnAccountMessage))
                    else -> onAction(ShowSnackbar(it))
                }
            }
            if (uiState.user != null) goToScreen("TaskList")
        }

        Column(
            modifier = modifier
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.dgomesdev_logo),
                contentDescription = stringResource(R.string.app_name)
            )
            Text(
                stringResource(R.string.log_in),
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.headlineMedium
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
                value = password,
                onValueChange = { setPassword(it) },
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
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        val user = User(email = email, password = password)
                        onAction(Login(user))
                    }
                )
            )

            Button(
                onClick = {
                    onAction(
                        Login(
                            User(email = email, password = password)
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEmailValid && password.isNotBlank()
            ) {
                Text(text = stringResource(R.string.log_in))
            }

            TextButton(onClick = { goToScreen("Register") }) {
                Text(
                    text = stringResource(R.string.don_t_have_an_account)
                )
            }

            TextButton(onClick = { goToScreen("ForgotPassword") }) {
                Text(
                    text = stringResource(R.string.recover_password)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthPreview() {
    LoginScreen(
        Modifier.fillMaxSize(),
        uiState = AppUiState(),
        onAction = {},
        goToScreen = {},
        accountManager = AccountManager(MainActivity()),
        scope = rememberCoroutineScope(),
    )
}