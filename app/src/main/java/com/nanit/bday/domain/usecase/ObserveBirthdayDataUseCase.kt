package com.nanit.bday.domain.usecase

import com.nanit.bday.domain.BirthdayData
import com.nanit.bday.domain.ConnectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBirthdayDataUseCase @Inject constructor(
    private val connectionRepository: ConnectionRepository
) {
    operator fun invoke(): Flow<BirthdayData?> {
        return connectionRepository.observeCachedBirthdayData()
    }
}
