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
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

class TaskServiceImpl(
    private val context: Context,
    private val securePreferences: SecurePreferences,
    private val http: HttpClient
) : TaskService {

    private val apiUrl = context.getString(R.string.api_url)

    override suspend fun saveTask(task: TaskRequestDto): Result<TaskResponseDto> {
        return try {
            val token = securePreferences.getToken() ?: error("No valid token")
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

            if (response.status == HttpStatusCode.Created) {
                val taskResponse = response.body<TaskResponseDto>()
                Log.i("Save task success", "$taskResponse")
                Result.success(taskResponse)
            } else {
                val error = if (response.status == HttpStatusCode.Forbidden) MessageDto(null)
                else response.body<MessageDto>()
                val errorMessage = when (response.status) {
                    HttpStatusCode.Unauthorized -> context.getString(R.string.closed_session)
                    HttpStatusCode.BadRequest -> context.getString(R.string.fill_mandatory_fields)
                    else -> error.message
                }
                Log.e("Save task error", "Error: ${errorMessage}, Status: ${response.status}")
                Result.failure(Exception("Error: ${errorMessage}, Status: ${response.status}"))
            }
        } catch (exception: Exception) {
            Log.e("Save task error", "Unexpected error: ${exception.message}")
            Result.failure(exception)
        }
    }

    override suspend fun updateTask(taskId: String, task: TaskRequestDto): Result<TaskResponseDto> {
        return try {
            val token = securePreferences.getToken() ?: error("No valid token")
            val response = http.client.patch {
                url {
                    protocol = URLProtocol.HTTPS
                    host = apiUrl
                    path("/tasks", taskId)
                }
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(task)
            }
            if (response.status == HttpStatusCode.OK) {
                val taskResponse = response.body<TaskResponseDto>()
                Log.i("Update task success", "$taskResponse")
                Result.success(taskResponse)
            } else {
                val error = if (response.status == HttpStatusCode.Forbidden) MessageDto(null)
                else response.body<MessageDto>()
                val errorMessage = when (response.status) {
                    HttpStatusCode.Unauthorized -> context.getString(R.string.closed_session)
                    HttpStatusCode.NotFound -> context.getString(R.string.task_not_found)
                    HttpStatusCode.BadRequest -> context.getString(R.string.fill_mandatory_fields)
                    else -> error.message
                }
                Log.e("Update task error", "Error: ${errorMessage}, Status: ${response.status}")
                Result.failure(Exception("Error: ${errorMessage}, Status: ${response.status}"))
            }
        } catch (exception: Exception) {
            Log.e("Register user error", "Unexpected error: ${exception.message}")
            Result.failure(exception)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<MessageDto> {
        return try {
            val token = securePreferences.getToken() ?: error("No valid token")
            val response = http.client.delete {
                url {
                    protocol = URLProtocol.HTTPS
                    host = apiUrl
                    path("/tasks", taskId)
                }
                bearerAuth(token)
            }

            if (response.status == HttpStatusCode.NoContent) {
                val deleteResponse =
                    MessageDto(context.getString(R.string.task_deleted))
                Log.i("Delete task success", "$deleteResponse")
                Result.success(deleteResponse)
            } else {
                val error = if (response.status == HttpStatusCode.Forbidden) MessageDto(null)
                else response.body<MessageDto>()
                val errorMessage = when (response.status) {
                    HttpStatusCode.Unauthorized -> context.getString(R.string.closed_session)
                    HttpStatusCode.NotFound -> context.getString(R.string.task_not_found)
                    else -> error.message
                }
                Log.e("Delete task error", "Error: ${errorMessage}, Status: ${response.status}")
                Result.failure(Exception("Error: ${errorMessage}, Status: ${response.status}"))
            }
        } catch (exception: Exception) {
            Log.e("Register user error", "Unexpected error: ${exception.message}")
            Result.failure(exception)
        }
    }

}