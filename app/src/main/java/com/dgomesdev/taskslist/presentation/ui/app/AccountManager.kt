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
import com.dgomesdev.taskslist.presentation.ui.features.auth.GetCredentialResult
import com.dgomesdev.taskslist.presentation.ui.features.auth.CreateCredentialResult
import com.dgomesdev.taskslist.presentation.ui.features.auth.CreateCredentialResult.Cancelled
import com.dgomesdev.taskslist.presentation.ui.features.auth.CreateCredentialResult.Failure
import com.dgomesdev.taskslist.presentation.ui.features.auth.CreateCredentialResult.Success

class AccountManager(
    private val activity: Activity
) {
    private val credentialManager = CredentialManager.create(activity)

    suspend fun createCredential(user: User): CreateCredentialResult {
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
            Cancelled(user, e.message.toString())
        } catch(e: CreateCredentialException) {
            Failure(user, e.message.toString())
        }
    }

    suspend fun getCredential(): GetCredentialResult {
        return try {
            val credentialResponse = credentialManager.getCredential(
                context = activity,
                request = GetCredentialRequest(
                    credentialOptions = listOf(GetPasswordOption())
                )
            )

            val credential = credentialResponse.credential as? PasswordCredential
                ?: return GetCredentialResult.Failure("")

            return GetCredentialResult.Success(User(email = credential.id, password = credential.password))
        } catch(e: GetCredentialCancellationException) {
            GetCredentialResult.Cancelled(e.message.toString())
        } catch(e: NoCredentialException) {
            GetCredentialResult.NoCredentials(e.message.toString())
        } catch(e: GetCredentialException) {
            GetCredentialResult.Failure(e.message.toString())
        }
    }
}