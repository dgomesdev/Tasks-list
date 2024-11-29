package com.dgomesdev.taskslist.presentation.ui.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.dgomesdev.taskslist.domain.model.UserAction
import com.dgomesdev.taskslist.presentation.viewmodel.AppUiState

@Composable
fun AuthScreen(
    modifier: Modifier,
    uiState: AppUiState
) {
    var isNewUser by rememberSaveable { mutableStateOf(true) }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordShown by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Surface {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.dgomesdev_logo),
                contentDescription = stringResource(R.string.app_name)
            )
            Text(
                text = if (isNewUser) stringResource(R.string.sign_up) else stringResource(R.string.log_in),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
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
                    Icon(
                        if (isPasswordShown) Icons.Default.VisibilityOff
                        else Icons.Default.Visibility,
                        contentDescription = stringResource(R.string.show_password)
                    )
                },
                visualTransformation =
                if (!isPasswordShown) PasswordVisualTransformation()
                else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        val user = User(username = username, password = password)
                        uiState.onUserChange(
                            if (isNewUser) UserAction.REGISTER else UserAction.LOGIN,
                            user
                        )
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val user = User(username = username, password = password)
                    uiState.onUserChange(
                        if (isNewUser) UserAction.REGISTER else UserAction.LOGIN,
                        user
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (isNewUser) stringResource(R.string.sign_up) else stringResource(R.string.log_in))
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { isNewUser = !isNewUser }) {
                Text(
                    text = if (isNewUser) stringResource(R.string.already_have_an_account) else stringResource(
                        R.string.don_t_have_an_account
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthPreview() {
    AuthScreen(
        Modifier.fillMaxSize(),
        uiState = AppUiState()
    )
}