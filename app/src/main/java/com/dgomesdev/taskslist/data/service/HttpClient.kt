package com.dgomesdev.taskslist.data.service

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

class HttpClient {
    val client by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
            install(Logging) {
                logger = io.ktor.client.plugins.logging.Logger.ANDROID
                level = io.ktor.client.plugins.logging.LogLevel.ALL
            }
        }
    }
}