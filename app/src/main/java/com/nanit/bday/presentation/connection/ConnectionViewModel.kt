package com.nanit.bday.presentation.connection

// ConnectionViewModel.kt
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanit.bday.domain.usecase.ConnectToServerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor (
    private val connectToServerUseCase: ConnectToServerUseCase,
    private val observeBirthdayDataUseCase: com.nanit.bday.domain.usecase.ObserveBirthdayDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConnectionUiState())
    val uiState: StateFlow<ConnectionUiState> = _uiState.asStateFlow()

    private val _sideEffects = Channel<ConnectionSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private var connectionJob: Job? = null
    private var birthdayObserverJob: Job? = null

    fun handleIntent(intent: ConnectionIntent) {
        when (intent) {
            is ConnectionIntent.UpdateIpAddress -> updateIpAddress(intent.ip)
            is ConnectionIntent.UpdatePort -> updatePort(intent.port)
            ConnectionIntent.ConnectClicked -> attemptConnection()
            ConnectionIntent.NavigationHandled -> handleNavigationComplete()
        }
    }

    private fun updateIpAddress(ip: String) {
        _uiState.update { currentState ->
            currentState.copy(
                ipAddress = ip,
                isConnectButtonEnabled = isValidInput(ip, currentState.port)
            )
        }
    }

    private fun updatePort(port: String) {
        // Only allow numeric input for port
        if (port.all { it.isDigit() }) {
            _uiState.update { currentState ->
                currentState.copy(
                    port = port,
                    isConnectButtonEnabled = isValidInput(currentState.ipAddress, port)
                )
            }
        }
    }

    private fun attemptConnection() {
        // Cancel any existing connection attempt
        connectionJob?.cancel()

        connectionJob = viewModelScope.launch {
            _uiState.update { it.copy(isConnecting = true, connectionResult = null) }

            try {
                val currentState = _uiState.value

                // Collect connection states until we reach a terminal state
                connectToServerUseCase.invoke(
                    currentState.ipAddress,
                    currentState.port.toIntOrNull() ?: 0
                )
                    .onEach { connectionState ->
                        Log.d("ConnectionViewModel", "Connection State: $connectionState")
                    }
                    // Wait for the first terminal state (Connected or Error)
                    .first { connectionState ->
                        when (connectionState) {
                            is com.nanit.bday.domain.ConnectionState.Connecting -> {
                                _uiState.update { it.copy(isConnecting = true) }
                                false // Keep waiting
                            }
                            is com.nanit.bday.domain.ConnectionState.Connected -> {
                                _uiState.update {
                                    it.copy(
                                        isConnecting = false,
                                        connectionResult = ConnectionResult.Success
                                    )
                                }
                                // Start observing birthday data from server
                                observeBirthdayData()
                                true // Terminal state reached, stop collecting
                            }
                            is com.nanit.bday.domain.ConnectionState.Error -> {
                                _uiState.update {
                                    it.copy(
                                        isConnecting = false,
                                        connectionResult = ConnectionResult.Error(connectionState.message)
                                    )
                                }
                                true // Terminal state reached, stop collecting
                            }
                            else -> false // Keep waiting for other states
                        }
                    }
            } catch (e: Exception) {
                Log.e("ConnectionViewModel", "Connection attempt failed", e)
                _uiState.update {
                    it.copy(
                        isConnecting = false,
                        connectionResult = ConnectionResult.Error(e.message ?: "Unknown error")
                    )
                }
            }
        }
    }

    private fun observeBirthdayData() {
        // Cancel any existing birthday observer
        birthdayObserverJob?.cancel()

        birthdayObserverJob = viewModelScope.launch {
            // Wait for the first non-null birthday data, then navigate
            observeBirthdayDataUseCase.invoke()
                .filterNotNull()
                .first() // Collect only the first non-null value, then stop

            // Send navigation event after receiving birthday data
            _sideEffects.send(ConnectionSideEffect.NavigateToHome)
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(connectionResult = null) }
    }

    private fun handleNavigationComplete() {
        _uiState.update { it.copy(connectionResult = null) }
    }

    private fun isValidInput(ip: String, port: String): Boolean {
        return isValidIpAddress(ip) && isValidPort(port)
    }

    private fun isValidIpAddress(ip: String): Boolean {
        val pattern = """^(\d{1,3}\.){3}\d{1,3}$""".toRegex()
        if (!pattern.matches(ip)) return false

        return ip.split(".").all {
            val num = it.toIntOrNull() ?: return false
            num in 0..255
        }
    }

    private fun isValidPort(port: String): Boolean {
        val portNum = port.toIntOrNull() ?: return false
        return portNum in 1..65535
    }

    override fun onCleared() {
        super.onCleared()
        // Cancel any ongoing operations when ViewModel is destroyed
        connectionJob?.cancel()
        birthdayObserverJob?.cancel()
    }
}