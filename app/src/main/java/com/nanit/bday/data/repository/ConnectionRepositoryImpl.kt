package com.nanit.bday.data.repository

import com.nanit.bday.data.dto.BirthdayDto
import com.nanit.bday.data.toDomainModel
import com.nanit.bday.data.source.WebSocketClient
import com.nanit.bday.data.toDomain
import com.nanit.bday.domain.BirthdayData
import com.nanit.bday.domain.BirthdayTheme
import com.nanit.bday.domain.ConnectionRepository
import com.nanit.bday.domain.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class ConnectionRepositoryImpl @Inject constructor(private val webSocketClient: WebSocketClient): ConnectionRepository {

    override suspend fun connectAndFetchData(
        ipAddress: String,
        port: Int
    ): Flow<ConnectionState> =
        webSocketClient.connect(ipAddress, port).map{ it.toDomainModel() }



    override fun getConnectionState(): Flow<ConnectionState> {
        return webSocketClient.connectionState.map { it.toDomainModel() }
    }

    override fun observeCachedBirthdayData(): Flow<BirthdayData?> = webSocketClient.birthdayInfo.map { it?.toDomain() }

}