package com.nanit.bday.data

import com.nanit.bday.data.dto.BirthdayDto
import com.nanit.bday.data.dto.ConnectionState
import com.nanit.bday.domain.BirthdayData
import com.nanit.bday.domain.BirthdayTheme
import java.util.Date


fun com.nanit.bday.data.dto.ConnectionState.toDomainModel(): com.nanit.bday.domain.ConnectionState {
    return when (this) {
        is ConnectionState.Idle -> com.nanit.bday.domain.ConnectionState.Idle
        is ConnectionState.Connecting -> com.nanit.bday.domain.ConnectionState.Connecting
        is ConnectionState.Connected -> com.nanit.bday.domain.ConnectionState.Connected
        is ConnectionState.Error -> com.nanit.bday.domain.ConnectionState.Error(this.message)
        is ConnectionState.Disconnected -> com.nanit.bday.domain.ConnectionState.Error("Disconnected")
    }
}


fun BirthdayDto.toDomain(): BirthdayData =
    BirthdayData(
        name = name,
        bDate = Date(dob),
        theme = BirthdayTheme.fromString(theme)
    )