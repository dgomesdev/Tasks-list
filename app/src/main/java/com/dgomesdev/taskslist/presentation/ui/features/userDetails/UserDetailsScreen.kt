package com.dgomesdev.taskslist.presentation.ui.features.userDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.dgomesdev.taskslist.domain.model.UserAction
import com.dgomesdev.taskslist.presentation.ui.app.HandleUserAction

@Composable
fun UserDetailsScreen(
    modifier: Modifier,
    user: User,
    handleUserAction: HandleUserAction,
    backToMainScreen: () -> Unit = {}
) {
    var username by rememberSaveable { mutableStateOf(user.username) }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isUpdating by remember { mutableStateOf(false) }
    var updatedUser = user.copy(username = username, password = password)

    Surface(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.edit_profile),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(stringResource(R.string.username)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.Face else Icons.Default.Lock,
                            contentDescription = if (showPassword) stringResource(R.string.hide_password) else stringResource(
                                R.string.show_password
                            )
                        )
                    }
                }
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
                        if (username.isNotBlank()) updatedUser = user.copy(username = username)
                        if (password.isNotBlank()) updatedUser = user.copy(password = password)
                        isUpdating = true
                        handleUserAction(UserAction.UPDATE, updatedUser)
                        isUpdating = false
                        backToMainScreen()
                    },
                    modifier = Modifier.padding(8.dp)
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
    UserDetailsScreen(
        modifier = Modifier.fillMaxSize(),
        user = User(username = "test", password = "test"),
        handleUserAction = { _, _ -> }
    )
}