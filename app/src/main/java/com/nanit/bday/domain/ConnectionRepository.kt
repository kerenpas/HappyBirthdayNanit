package com.nanit.bday.domain

import kotlinx.coroutines.flow.Flow

interface ConnectionRepository {
    fun connectAndFetchData(ipAddress: String, port: Int): Flow<Result<BirthdayData>>
    fun getConnectionState(): Flow<ConnectionState>
    fun observeCachedBirthdayData(): Flow<BirthdayData?>
}