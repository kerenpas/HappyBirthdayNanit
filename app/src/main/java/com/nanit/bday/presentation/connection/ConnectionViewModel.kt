package com.nanit.bday.presentation.connection

// ConnectionViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanit.bday.domain.usecase.ConnectToServerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor (
    private val connectToServerUseCase: ConnectToServerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConnectionUiState())
    val uiState: StateFlow<ConnectionUiState> = _uiState.asStateFlow()

    private val _sideEffects = Channel<ConnectionSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    fun handleIntent(intent: ConnectionIntent) {
        when (intent) {
            is ConnectionIntent.UpdateIpAddress -> updateIpAddress(intent.ip)
            is ConnectionIntent.UpdatePort -> updatePort(intent.port)
            ConnectionIntent.ConnectClicked -> attemptConnection()
            ConnectionIntent.ClearError -> clearError()
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
        viewModelScope.launch {
            _uiState.update { it.copy(isConnecting = true, connectionResult = null) }

            try {
                val currentState = _uiState.value

                _uiState.update { currentState -> currentState.copy(isConnecting = true) }


                connectToServerUseCase.invoke(
                    currentState.ipAddress,
                    currentState.port.toIntOrNull() ?: 0
                ).collect { result ->
                    result.onSuccess {
                        _uiState.update {
                            it.copy(
                                isConnecting = false,
                                connectionResult = ConnectionResult.Success
                            )
                        }
                        _sideEffects.send(ConnectionSideEffect.NavigateToHome)
                    }.onFailure { error ->
                        _uiState.update {
                            it.copy(
                                isConnecting = false,
                                connectionResult = ConnectionResult.Error(
                                    error.message ?: "Unknown error"
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isConnecting = false,
                        connectionResult = ConnectionResult.Error(e.message ?: "Unknown error")
                    )
                }

            }
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
}