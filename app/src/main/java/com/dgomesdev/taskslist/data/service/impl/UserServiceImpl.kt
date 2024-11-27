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
        val response = http.client.post {
            url {
                protocol = URLProtocol.HTTPS
                host = apiUrl
                path("/register")
            }
            contentType(ContentType.Application.Json)
            setBody(user)
        }

        return try {
            val authResponse = response.body<AuthResponseDto>()
            Log.i("Register user success", "$authResponse")
            Result.success(authResponse)
        } catch (e: Exception) {
            try {
                val errorResponse = response.body<MessageDto>()
                Log.e("Register user error", "Error: ${errorResponse.message}")
                Log.e("Register user error", "Exception: ${e.message}")
                Result.failure(Exception(errorResponse.message))
            } catch (innerException: Exception) {
                Log.e("Register user error", "Unexpected error: ${innerException.message}")
                Result.failure(innerException)
            }
        }
    }

    override suspend fun loginUser(user: AuthRequestDto): Result<AuthResponseDto> {
        Log.i("Login User", "User: $user")
        val response = http.client.post {
            url {
                protocol = URLProtocol.HTTPS
                host = apiUrl
                path("/login")
            }
            contentType(ContentType.Application.Json)
            setBody(user)
        }
        return try {
            val authResponse = response.body<AuthResponseDto>()
            Log.i("Login user success", "$authResponse")
            Result.success(authResponse)
        } catch (e: Exception) {
            try {
                val errorResponse = response.body<MessageDto>()
                Log.e("Login user error", "Error: ${errorResponse.message}")
                Log.e("Register user error", "Exception: ${e.message}")
                Result.failure(Exception(errorResponse.message))
            } catch (innerException: Exception) {
                Log.e("Login user error", "Unexpected error: ${innerException.message}")
                Result.failure(innerException)
            }
        }
    }

    override suspend fun getUser(userId: String): Result<UserResponseDto> {
        val token = securePreferences.getToken() ?: error("No valid token")
        val response = http.client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = apiUrl
                path("/user", userId) // Dynamic path parameter
            }
            bearerAuth(token)
        }
        return try {
            val userResponse = response.body<UserResponseDto>()
            Log.i("Get user success", "$userResponse")
            Result.success(userResponse)
        } catch (e: Exception) {
            try {
                val errorResponse = response.body<MessageDto>()
                Log.e("Get user error", "Error: ${errorResponse.message}")
                Log.e("Register user error", "Exception: ${e.message}")
                Result.failure(Exception(errorResponse.message))
            } catch (innerException: Exception) {
                Log.e("Get user error", "Unexpected error: ${innerException.message}")
                Result.failure(innerException)
            }
        }
    }

    override suspend fun updateUser(userId: String, user: UserRequestDto): Result<UserResponseDto> {
        val token = securePreferences.getToken() ?: error("No valid token")
        val response = http.client.patch {
            url {
                protocol = URLProtocol.HTTPS
                host = apiUrl
                path("/user", userId) // Dynamic path parameter
            }
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(user)
        }

        return try {
            val userResponse = response.body<UserResponseDto>()
            Log.i("Update user success", "$userResponse")
            Result.success(userResponse)
        } catch (e: Exception) {
            try {
                val errorResponse = response.body<MessageDto>()
                Log.e("Update user error", "Error: ${errorResponse.message}")
                Log.e("Register user error", "Exception: ${e.message}")
                Result.failure(Exception(errorResponse.message))
            } catch (innerException: Exception) {
                Log.e("Update user error", "Unexpected error: ${innerException.message}")
                Result.failure(innerException)
            }
        }
    }

    override suspend fun deleteUser(userId: String): Result<MessageDto> {
        val token = securePreferences.getToken() ?: error("No valid token")
        val response = http.client.delete {
            url {
                protocol = URLProtocol.HTTPS
                host = apiUrl
                path("/user", userId) // Dynamic path parameter
            }
            bearerAuth(token)
        }
        return try {
            val deleteResponse = MessageDto(context.getString(R.string.account_deleted))
            Log.i("Delete user success", deleteResponse.message)
            Result.success(deleteResponse)
        } catch (e: Exception) {
            try {
                val errorResponse = response.body<MessageDto>()
                Log.e("Delete user error", "Error: ${errorResponse.message}")
                Log.e("Register user error", "Exception: ${e.message}")
                Result.failure(Exception(errorResponse.message))
            } catch (innerException: Exception) {
                Log.e("Delete user error", "Unexpected error: ${innerException.message}")
                Result.failure(innerException)
            }
        }
    }
}