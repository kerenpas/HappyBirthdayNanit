package com.nanit.bday.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class BirthdayDataResponse(
    val name: String,
    val dob: Long,
    val theme: String
)