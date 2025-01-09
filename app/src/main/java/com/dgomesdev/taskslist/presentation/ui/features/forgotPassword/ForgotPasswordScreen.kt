package com.dgomesdev.taskslist.presentation.ui.features.forgotPassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.presentation.ui.app.EMAIL_PATTERN
import com.dgomesdev.taskslist.presentation.ui.app.OnAction
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.RecoverPassword
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent.ShowSnackbar
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier,
    uiState: AppUiState,
    onAction: OnAction,
    backToMainScreen: () -> Unit,
    goToScreen: (String) -> Unit
) {
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    val (isEmailValid, setIsEmailValid) = rememberSaveable { mutableStateOf(true) }

    Scaffold(
        snackbarHost = { SnackbarHost(uiState.snackbarHostState) }
    ) { padding ->
        LaunchedEffect(uiState.message) {
            uiState.message?.let {
                if (!it.contains("403")) ShowSnackbar(it)
                //onAction(RefreshMessage)
            }
        }

        Column(
            modifier = modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.recover_password),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
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
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(onDone = {
                    onAction(RecoverPassword(User(email = email)))
                    backToMainScreen()
                }),
                isError = !isEmailValid,
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
                    onAction(RecoverPassword(User(email = email)))
                    backToMainScreen()
                },
                modifier = Modifier.padding(8.dp),
                enabled = email.isNotBlank() && isEmailValid
            ) {
                Text(text = stringResource(R.string.send_recovery_link))
            }

            TextButton(onClick = { goToScreen("ResetPassword") }) {
                Text(
                    text = stringResource(R.string.already_have_a_code)
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UpdatePreview() {
    ForgotPasswordScreen(
        modifier = Modifier.fillMaxSize(),
        uiState = AppUiState(),
        onAction = {},
        backToMainScreen = {},
        goToScreen = {},
    )
}