package com.dgomesdev.taskslist.presentation.ui.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.presentation.ui.app.OnAction
import com.dgomesdev.taskslist.presentation.ui.app.ShowSnackbar
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent

@Composable
fun LoginScreen(
    modifier: Modifier,
    onAction: OnAction,
    goToScreen: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    showSnackbar: ShowSnackbar,
    message: String?
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordShown by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        LaunchedEffect(message) {
            message?.let {
                if (!it.contains("403")) showSnackbar(message)
            }
        }

        Column(
            modifier = modifier.padding(padding).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.dgomesdev_logo),
                contentDescription = stringResource(R.string.app_name)
            )
            Text(
                stringResource(R.string.log_in),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.email)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = stringResource(R.string.email)
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                })
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.password)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = stringResource(R.string.password)
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { isPasswordShown = !isPasswordShown }
                    ) {
                        Icon(
                            if (isPasswordShown) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility,
                            contentDescription = stringResource(R.string.show_password)
                        )
                    }
                },
                visualTransformation =
                if (!isPasswordShown) PasswordVisualTransformation()
                else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        val user = User(email = email, password = password)
                        onAction(AppUiIntent.Login(user))
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val user = User(email = email, password = password)
                    onAction(AppUiIntent.Login(user))
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.isNotBlank() && password.isNotBlank()
            ) {
                Text(text = stringResource(R.string.log_in))
            }

            TextButton(onClick = { goToScreen("Register") }) {
                Text(
                    text = stringResource(R.string.don_t_have_an_account)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

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
        onAction = {},
        goToScreen = {},
        snackbarHostState = SnackbarHostState(),
        showSnackbar = {},
        message = null
    )
}