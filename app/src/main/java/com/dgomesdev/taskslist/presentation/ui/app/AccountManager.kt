package com.dgomesdev.taskslist.presentation.ui.app

import android.app.Activity
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.dgomesdev.taskslist.domain.model.User
import com.dgomesdev.taskslist.presentation.ui.features.auth.LoginResult
import com.dgomesdev.taskslist.presentation.ui.features.auth.RegisterResult
import com.dgomesdev.taskslist.presentation.ui.features.auth.RegisterResult.Cancelled
import com.dgomesdev.taskslist.presentation.ui.features.auth.RegisterResult.Failure
import com.dgomesdev.taskslist.presentation.ui.features.auth.RegisterResult.Success

class AccountManager(
    private val activity: Activity
) {
    private val credentialManager = CredentialManager.create(activity)

    suspend fun signUp(user: User): RegisterResult {
        return try {
            credentialManager.createCredential(
                context = activity,
                request = CreatePasswordRequest(
                    id = user.email,
                    password = user.password
                )
            )
            Success(user)
        } catch (e: CreateCredentialCancellationException) {
            Cancelled(e.message.toString())
        } catch(e: CreateCredentialException) {
            Failure(e.message.toString())
        }
    }

    suspend fun signIn(): LoginResult {
        return try {
            val credentialResponse = credentialManager.getCredential(
                context = activity,
                request = GetCredentialRequest(
                    credentialOptions = listOf(GetPasswordOption())
                )
            )

            val credential = credentialResponse.credential as? PasswordCredential
                ?: return LoginResult.Failure("")

            return LoginResult.Success(User(email = credential.id, password = credential.password))
        } catch(e: GetCredentialCancellationException) {
            LoginResult.Cancelled(e.message.toString())
        } catch(e: NoCredentialException) {
            LoginResult.NoCredentials(e.message.toString())
        } catch(e: GetCredentialException) {
            LoginResult.Failure(e.message.toString())
        }
    }
}