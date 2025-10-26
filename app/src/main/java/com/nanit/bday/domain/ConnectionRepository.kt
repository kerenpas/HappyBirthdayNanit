package com.nanit.bday.domain

import kotlinx.coroutines.flow.Flow

interface ConnectionRepository {
    suspend fun connectAndFetchData(ipAddress: String, port: Int): Flow<ConnectionState>
    fun getConnectionState(): Flow<ConnectionState>
    fun observeCachedBirthdayData(): Flow<BirthdayData?>
}