package com.dgomesdev.taskslist.data.service.impl

import android.content.Context
import android.util.Log
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.data.dto.request.AuthRequestDto
import com.dgomesdev.taskslist.data.dto.request.UserRequestDto
import com.dgomesdev.taskslist.data.dto.response.AuthResponseDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.UserResponseDto
import com.dgomesdev.taskslist.data.service.HttpClient
import com.dgomesdev.taskslist.data.service.UserService
import com.dgomesdev.taskslist.infra.SecurePreferences
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

class UserServiceImpl(
    private val context: Context,
    private val securePreferences: SecurePreferences,
    private val http: HttpClient
) : UserService {

    private val apiUrl = context.getString(R.string.api_url)

    override suspend fun registerUser(user: AuthRequestDto): Result<AuthResponseDto> {
        Log.i("Register User", "User: $user")
        return try {
            val response = http.client.post {
                url {
                    protocol = URLProtocol.HTTPS
                    host = apiUrl
                    path("/register")
                }
                contentType(ContentType.Application.Json)
                setBody(user)
            }

            if (response.status == HttpStatusCode.Created) {
                val authResponse = response.body<AuthResponseDto>()
                Log.i("Register user success", "$authResponse")
                Result.success(authResponse)
            } else {
                val error = response.body<MessageDto>()
                val errorMessage = when (response.status) {
                    HttpStatusCode.Conflict -> context.getString(R.string.user_already_exists)
                    HttpStatusCode.BadRequest -> context.getString(R.string.fill_mandatory_fields)
                    else -> error.message
                }
                Log.e("Register user error", "Error: ${errorMessage}, Status: ${response.status}")
                Result.failure(Exception("Error: ${errorMessage}, Status: ${response.status}"))
            }
        } catch (exception: Exception) {
            Log.e("Register user error", "Unexpected error: ${exception.message}")
            Result.failure(exception)
        }
    }

    override suspend fun loginUser(user: AuthRequestDto): Result<AuthResponseDto> {
        Log.i("Login User", "User: $user")
        return try {
            val response = http.client.post {
                url {
                    protocol = URLProtocol.HTTPS
                    host = apiUrl
                    path("/login")
                }
                contentType(ContentType.Application.Json)
                setBody(user)
            }

            if (response.status == HttpStatusCode.OK) {
                val authResponse = response.body<AuthResponseDto>()
                Log.i("Login user success", "$authResponse")
                Result.success(authResponse)
            } else {
                val error = response.body<MessageDto>()
                val errorMessage = when (response.status) {
                    HttpStatusCode.NotAcceptable -> context.getString(R.string.user_does_not_exist)
                    HttpStatusCode.BadRequest -> context.getString(R.string.fill_mandatory_fields)
                    else -> error.message
                }
                Log.e("Register user error", "Error: ${errorMessage}, Status: ${response.status}")
                Result.failure(Exception("Error: ${errorMessage}, Status: ${response.status}"))
            }
        } catch (exception: Exception) {
            Log.e("Login user error", "Unexpected error: ${exception.message}")
            Result.failure(exception)
        }
    }

    override suspend fun getUser(userId: String): Result<UserResponseDto> {
        return try {
            val token = securePreferences.getToken() ?: error("No valid token")
            val response = http.client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = apiUrl
                    path("/user", userId)
                }
                bearerAuth(token)
            }

            if (response.status == HttpStatusCode.Found) {
                val userResponse = response.body<UserResponseDto>()
                Log.i("Get user success", "$userResponse")
                Result.success(userResponse)
            } else {
                val error = response.body<MessageDto>()
                val errorMessage = when (response.status) {
                    HttpStatusCode.Forbidden, HttpStatusCode.Unauthorized -> context.getString(R.string.closed_session)
                    HttpStatusCode.NotFound -> context.getString(R.string.user_does_not_exist)
                    else -> error.message
                }
                Log.e("Get user error", "Error: ${errorMessage}, Status: ${response.status}")
                Result.failure(Exception("Error: ${errorMessage}, Status: ${response.status}"))
            }
        } catch (exception: Exception) {
            Log.e("Get user error", "Unexpected error: ${exception.message}")
            Result.failure(exception)
        }

    }

    override suspend fun updateUser(userId: String, user: UserRequestDto): Result<UserResponseDto> {
        return try {
            val token = securePreferences.getToken() ?: error("No valid token")
            val response = http.client.patch {
                url {
                    protocol = URLProtocol.HTTPS
                    host = apiUrl
                    path("/user", userId)
                }
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(user)
            }

            if (response.status == HttpStatusCode.OK) {
                val userResponse = response.body<UserResponseDto>()
                Log.i("Update user success", "$userResponse")
                Result.success(userResponse)
            } else {
                val error = response.body<MessageDto>()
                val errorMessage = when (response.status) {
                    HttpStatusCode.Forbidden, HttpStatusCode.Unauthorized -> context.getString(R.string.closed_session)
                    HttpStatusCode.NotFound -> context.getString(R.string.user_does_not_exist)
                    HttpStatusCode.Conflict -> context.getString(R.string.user_already_exists)
                    HttpStatusCode.BadRequest -> context.getString(R.string.fill_mandatory_fields)
                    else -> error.message
                }
                Log.e("Update user error", "Error: ${response.body<String>()}")
                Result.failure(Exception("Error: ${errorMessage}, Status: ${response.status}"))
            }
        } catch (exception: Exception) {
            Log.e("Update user error", "Unexpected error: ${exception.message}")
            Result.failure(exception)

        }
    }

    override suspend fun deleteUser(userId: String): Result<MessageDto> {
        return try {
            val token = securePreferences.getToken() ?: error("No valid token")
            val response = http.client.delete {
                url {
                    protocol = URLProtocol.HTTPS
                    host = apiUrl
                    path("/user", userId)
                }
                bearerAuth(token)
            }

            if (response.status == HttpStatusCode.NoContent) {
                val deleteResponse =
                    MessageDto(context.getString(R.string.account_deleted))
                Log.i("Delete user success", deleteResponse.message.toString())
                Result.success(deleteResponse)
            } else {
                val error = response.body<MessageDto>()
                val errorMessage = when (response.status) {
                    HttpStatusCode.Forbidden, HttpStatusCode.Unauthorized -> context.getString(R.string.closed_session)
                    HttpStatusCode.NotFound -> context.getString(R.string.user_does_not_exist)
                    else -> error.message
                }
                Log.e("Delete user error", "Error: ${errorMessage}, Status: ${response.status}")
                Result.failure(Exception("Error: ${errorMessage}, Status: ${response.status}"))
            }
        } catch (exception: Exception) {
            Log.e("Delete user error", "Unexpected error: ${exception.message}")
            Result.failure(exception)
        }
    }
}