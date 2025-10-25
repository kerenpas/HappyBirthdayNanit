package com.nanit.bday.data.repository

import com.nanit.bday.data.source.WebSocketClient
import com.nanit.bday.domain.BirthdayData
import com.nanit.bday.domain.BirthdayTheme
import com.nanit.bday.domain.ConnectionRepository
import com.nanit.bday.domain.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Date
import javax.inject.Inject

class ConnectionRepositoryImpl @Inject constructor(private val webSocketClient: WebSocketClient): ConnectionRepository {

    override fun connectAndFetchData(
        ipAddress: String,
        port: Int
    ): Flow<Result<BirthdayData>> {
        webSocketClient.connect(ipAddress, port)

        val data= BirthdayData(name = "keren", bDate = Date(), theme = BirthdayTheme.PELICAN)
        return flowOf(Result.success(data));
    }

    override fun getConnectionState(): Flow<ConnectionState> {
        return flowOf(ConnectionState.Connected)

    }


    override fun observeCachedBirthdayData(): Flow<BirthdayData?> {
        val data= BirthdayData(name = "keren", bDate = Date(), theme = BirthdayTheme.PELICAN)
        return flowOf(data)

    }

}