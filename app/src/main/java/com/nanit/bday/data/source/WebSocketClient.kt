package com.nanit.bday.data.source

import android.util.Log
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import com.nanit.bday.data.dto.BirthdayDto
import com.nanit.bday.data.dto.ConnectionState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketClient @Inject constructor(private val client: HttpClient) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var session: WebSocketSession? = null
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _birthdayDataResponse = MutableStateFlow<BirthdayDto?>(null)
    val birthdayInfo: StateFlow<BirthdayDto?> = _birthdayDataResponse.asStateFlow()

    suspend fun connect(ipAddress: String, port: Int = 8080): StateFlow<ConnectionState> {
        Log.d("WebSocketClient", "Connecting to $ipAddress:$port")
        _connectionState.value = ConnectionState.Connecting

        try {
            session = client.webSocketSession(
                method = HttpMethod.Get,
                host = ipAddress,
                port = port,
                path = "/nanit"
            )

            Log.d("WebSocketClient", "WebSocket session established: $session")

            _connectionState.value = ConnectionState.Connected

            // Start listening for messages in a separate coroutine (non-blocking)
            scope.launch {
                try {
                    listenForMessages()
                } catch (e: Exception) {
                    Log.e("WebSocketClient", "Error in message listener", e)
                    _connectionState.value = ConnectionState.Error(e.message ?: "Connection lost")
                }
            }
        } catch (e: Exception) {
            Log.e("WebSocketClient", "Connection failed", e)
            _connectionState.value = ConnectionState.Error(e.message ?: "Connection failed")
        }
        return connectionState
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
                _birthdayDataResponse.value = null
            } else {
                val info = Json.decodeFromString<BirthdayDto>(message)
                _birthdayDataResponse.value = info
            }
        } catch (e: Exception) {
            // Log parsing error but don't crash
            Log.e("WebSocketClient", "Error parsing message: $message", e)
            _birthdayDataResponse.value = null
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
        scope.cancel()
        client.close()
    }
}
