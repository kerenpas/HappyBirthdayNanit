package com.nanit.bday.domain

sealed class ConnectionState {
    object Idle : ConnectionState()
    object Connecting : ConnectionState()
    object Connected : ConnectionState()
    object MessageReceived : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}