package com.dgomesdev.taskslist.presentation.ui.features.forgotPassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.presentation.ui.app.OnAction
import com.dgomesdev.taskslist.presentation.ui.app.ShowSnackbar
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiIntent

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier,
    message: String?,
    onAction: OnAction,
    backToMainScreen: () -> Unit,
    snackbarHostState: SnackbarHostState,
    showSnackbar: ShowSnackbar,
    goToScreen: (String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var isUpdating by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        LaunchedEffect(message) {
            message?.let { if (!it.contains("403")) showSnackbar(message) }
        }

        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.recover_password),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
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
                        if (email.isNotBlank())
                            onAction(AppUiIntent.RecoverPassword(User(email = email)))
                        isUpdating = false
                        backToMainScreen()
                    },
                    modifier = Modifier.padding(8.dp),
                    enabled = email.isNotBlank()
                ) {
                    Text(text = stringResource(R.string.send_recovery_link))
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { goToScreen("ResetPassword") }) {
                    Text(
                        text = stringResource(R.string.already_have_a_code)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UpdatePreview() {
    ForgotPasswordScreen(
        modifier = Modifier.fillMaxSize(),
        onAction = {},
        backToMainScreen = {},
        snackbarHostState = SnackbarHostState(),
        showSnackbar = {},
        goToScreen = {},
        message = null,
    )
}