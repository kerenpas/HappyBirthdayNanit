package com.nanit.bday.domain.usecase

import com.nanit.bday.domain.BirthdayData
import com.nanit.bday.domain.ConnectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConnectToServerUseCase @Inject constructor(
    private val connectionRepository: ConnectionRepository
) {
    operator fun invoke(ipAddress: String, port: Int): Flow<Result<BirthdayData>> {
        return connectionRepository.connectAndFetchData(ipAddress, port)
    }
}