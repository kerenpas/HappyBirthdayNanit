package com.nanit.bday.data.source

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import com.nanit.bday.data.dto.BirthdayDataResponse
import com.nanit.bday.data.dto.ConnectionState
import javax.inject.Inject

class WebSocketClient @Inject constructor(private val client: HttpClient) {
   /* private val client = HttpClient(Android) {
        install(WebSockets) {
            maxFrameSize = Long.MAX_VALUE
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }*/

    private var session: WebSocketSession? = null
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _birthdayDataResponse = Channel<BirthdayDataResponse?>()
    val birthdayInfo: Flow<BirthdayDataResponse?> = _birthdayDataResponse.receiveAsFlow()

    suspend fun connect(ipAddress: String, port: Int = 8080) {
        _connectionState.value = ConnectionState.Connecting

        try {
            session = client.webSocketSession(
                method = HttpMethod.Get,
                host = ipAddress,
                port = port,
                path = "/nanit"
            )

            _connectionState.value = ConnectionState.Connected

            // Start listening for incoming messages
            listenForMessages()

        } catch (e: Exception) {
            _connectionState.value = ConnectionState.Error(e.message ?: "Connection failed")
        }
    }

    private suspend fun listenForMessages() {
        session?.let { ws ->
            try {
                for (frame in ws.incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            val text = frame.readText()
                            handleMessage(text)
                        }
                        is Frame.Close -> {
                            _connectionState.value = ConnectionState.Disconnected
                        }
                        else -> {}
                    }
                }
            } catch (e: Exception) {
                _connectionState.value = ConnectionState.Error(e.message ?: "Error receiving messages")
            }
        }
    }

    private suspend fun handleMessage(message: String) {
        try {
            if (message == "null") {
                _birthdayDataResponse.send(null)
            } else {
                val info = Json.decodeFromString<BirthdayDataResponse>(message)
                _birthdayDataResponse.send(info)
            }
        } catch (e: Exception) {
            // Log parsing error but don't crash
            Log.e("WebSocketClient", "Error parsing message: $message", e)
            _birthdayDataResponse.send(null)
        }
    }

    suspend fun requestBirthdayInfo() {
        session?.let { ws ->
            try {
                ws.send(Frame.Text("HappyBirthday"))
            } catch (e: Exception) {
                _connectionState.value = ConnectionState.Error("Failed to send message: ${e.message}")
            }
        }
    }

    suspend fun disconnect() {
        try {
            session?.close()
            session = null
            _connectionState.value = ConnectionState.Disconnected
        } catch (e: Exception) {
            // Ignore close errors
        }
    }

    fun close() {
        client.close()
    }
}
