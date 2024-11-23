package com.dgomesdev.taskslist.data.service.impl

import android.content.Context
import android.util.Log
import com.dgomesdev.taskslist.R
import com.dgomesdev.taskslist.data.dto.request.TaskRequestDto
import com.dgomesdev.taskslist.data.dto.response.MessageDto
import com.dgomesdev.taskslist.data.dto.response.TaskResponseDto
import com.dgomesdev.taskslist.data.service.HttpClient
import com.dgomesdev.taskslist.data.service.TaskService
import com.dgomesdev.taskslist.infra.SecurePreferences
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

class TaskServiceImpl(
    context: Context,
    private val securePreferences: SecurePreferences,
    private val http: HttpClient
) : TaskService {

    private val apiUrl = context.getString(R.string.api_url)

    override suspend fun saveTask(task: TaskRequestDto): Result<TaskResponseDto> {
        val token = securePreferences.getToken() ?: error("No valid token")
        Log.i("Save task", "Task: $task")
        Log.i("Save task", "Token: $token")
        val response = http.client.post {
            url {
                protocol = URLProtocol.HTTPS
                host = apiUrl
                path("/tasks")
            }
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(task)
        }
        return try {
            val taskResponse = response.body<TaskResponseDto>()
            Log.i("Save task success", "$taskResponse")
            Result.success(taskResponse)
        } catch (e: Exception) {
            try {
                val errorResponse = response.body<MessageDto>()
                Log.e("Save task error", "Error: ${errorResponse.message}")
                Result.failure(Exception(errorResponse.message))
            } catch (innerException: Exception) {
                val rawResponse = response.bodyAsText()
                Log.e("Save task error", "Raw response: $rawResponse")
                Log.e("Save task error", "Unexpected error: ${innerException.message}")
                Result.failure(innerException)
            }
        }
    }

    override suspend fun getTask(taskId: String): Result<TaskResponseDto> {
        val token = securePreferences.getToken() ?: error("No valid token")
        val response = http.client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = apiUrl
                path("/tasks/", taskId) // Dynamic path parameter
            }
            bearerAuth(token)
        }
        return try {
            val taskResponse = response.body<TaskResponseDto>()
            Log.i("Get task success", "$taskResponse")
            Result.success(taskResponse)
        } catch (e: Exception) {
            try {
                val errorResponse = response.body<MessageDto>()
                Log.e("Get task error", "Error: ${errorResponse.message}")
                Result.failure(Exception(errorResponse.message))
            } catch (innerException: Exception) {
                Log.e("Get task error", "Unexpected error: ${innerException.message}")
                Result.failure(innerException)
            }
        }
    }

    override suspend fun updateTask(taskId: String, task: TaskRequestDto): Result<TaskResponseDto> {
        val token = securePreferences.getToken() ?: error("No valid token")
        val response = http.client.patch {
            url {
                protocol = URLProtocol.HTTPS
                host = apiUrl
                path("/tasks/", taskId) // Dynamic path parameter
            }
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(task)
        }
        return try {
            val taskResponse = response.body<TaskResponseDto>()
            Log.i("Update task success", "$taskResponse")
            Result.success(taskResponse)
        } catch (e: Exception) {
            try {
                val errorResponse = response.body<MessageDto>()
                Log.e("Update task error", "Error: ${errorResponse.message}")
                Result.failure(Exception(errorResponse.message))
            } catch (innerException: Exception) {
                Log.e("Update task error", "Unexpected error: ${innerException.message}")
                Result.failure(innerException)
            }
        }
    }

    override suspend fun deleteTask(taskId: String): Result<MessageDto> {
        val token = securePreferences.getToken() ?: error("No valid token")
        val response = http.client.delete {
            url {
                protocol = URLProtocol.HTTPS
                host = apiUrl
                path("/tasks/", taskId) // Dynamic path parameter
            }
            bearerAuth(token)
        }
        return try {
            val deleteResponse = response.body<MessageDto>()
            Log.i("Delete task success", "$deleteResponse")
            Result.success(deleteResponse)
        } catch (e: Exception) {
            try {
                val errorResponse = response.body<MessageDto>()
                Log.e("Delete task error", "Error: ${errorResponse.message}")
                Result.failure(Exception(errorResponse.message))
            } catch (innerException: Exception) {
                Log.e("Delete task error", "Unexpected error: ${innerException.message}")
                Result.failure(innerException)
            }
        }
    }
}