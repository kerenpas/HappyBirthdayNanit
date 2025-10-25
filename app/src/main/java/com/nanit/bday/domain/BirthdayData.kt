package com.nanit.bday.domain

import java.util.Date

data class BirthdayData(
    val name: String,
    val bDate: Date,
    val theme: BirthdayTheme
)


enum class BirthdayTheme {
    PELICAN,
    FOX,
    ELEPHANT;

    companion object {
        fun fromString(theme: String): BirthdayTheme = when (theme.lowercase()) {
            "pelican" -> PELICAN
            "fox" -> FOX
            "elephant" -> ELEPHANT
            else -> PELICAN
        }
    }
}