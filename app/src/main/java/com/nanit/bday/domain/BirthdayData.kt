package com.nanit.bday.domain
import java.util.Calendar
import java.util.Date

data class BirthdayData(
    val name: String,
    val bDate: Date,
    val theme: BirthdayTheme
) {
    val age: Age get() = calculateAge(bDate)
}


data class Age(
    val value: Int,
    val unit: AgeUnit
)

enum class AgeUnit {
    MONTHS,
    YEARS
}


fun calculateAge(birthDate: Date): Age {
    val today = Calendar.getInstance()
    val birth = Calendar.getInstance().apply { time = birthDate }

    var years = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
    var months = today.get(Calendar.MONTH) - birth.get(Calendar.MONTH)

    if (months < 0 || (months == 0 && today.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH))) {
        years--
        months += 12
    }

    val totalMonths = years * 12 + months

    val cappedYears = years.coerceAtMost(9)
    val cappedMonths = totalMonths.coerceAtMost(9 * 12)

    return if (cappedYears < 2) {
        Age(value = cappedMonths, unit = AgeUnit.MONTHS)
    } else {
        Age(value = cappedYears, unit = AgeUnit.YEARS)
    }
}
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