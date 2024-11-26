package com.dgomesdev.taskslist.presentation.ui.features.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.domain.model.UserAction
import com.dgomesdev.taskslist.presentation.ui.app.HandleUserAction

@Composable
fun AuthScreen(
    modifier: Modifier,
    handleUserAction: HandleUserAction
    ) {
    var isNewUser by rememberSaveable { mutableStateOf(true) }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Surface(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isNewUser) "Sign In" else "Log In",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Username Input
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Username") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val user = User(username = username, password = password)
                    handleUserAction(
                        if (isNewUser) UserAction.REGISTER else UserAction.LOGIN,
                        user
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (isNewUser) "Sign In" else "Log In")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { isNewUser = !isNewUser }) {
                Text(
                    text = if (isNewUser) "Already have an account? Log In" else "Don't have an account? Sign In"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthPreview() {
    AuthScreen(
        Modifier.fillMaxSize(),
        handleUserAction = { _, _ -> }
    )
}