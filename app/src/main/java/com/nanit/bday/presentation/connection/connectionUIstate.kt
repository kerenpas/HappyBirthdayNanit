package com.nanit.bday.presentation.connection

data class ConnectionUiState(
    val ipAddress: String = "",
    val port: String = "",
    val isConnecting: Boolean = false,
    val connectionResult: ConnectionResult? = null,
    val isConnectButtonEnabled: Boolean = false,
    val error: String? = null,
)

sealed interface ConnectionResult {
    object Success : ConnectionResult
    data class Error(val message: String) : ConnectionResult
}

sealed interface ConnectionIntent {
    data class UpdateIpAddress(val ip: String) : ConnectionIntent
    data class UpdatePort(val port: String) : ConnectionIntent
    object ConnectClicked : ConnectionIntent
    object NavigationHandled : ConnectionIntent
}

sealed interface ConnectionSideEffect {
    object NavigateToHome : ConnectionSideEffect
}