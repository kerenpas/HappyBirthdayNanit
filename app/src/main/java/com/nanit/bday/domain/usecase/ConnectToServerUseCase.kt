package com.nanit.bday.domain.usecase

import com.nanit.bday.domain.ConnectionRepository
import com.nanit.bday.domain.ConnectionState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConnectToServerUseCase @Inject constructor(
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(ipAddress: String, port: Int): Flow<ConnectionState> {
        return connectionRepository.connectAndFetchData(ipAddress, port)
    }
}