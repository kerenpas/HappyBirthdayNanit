package com.nanit.bday.data.dto

sealed class ConnectionState {
    object Idle : ConnectionState()
    object Connecting : ConnectionState()
    object Connected : ConnectionState()
    data class Error(val message: String) : ConnectionState()
    object Disconnected : ConnectionState()
}
