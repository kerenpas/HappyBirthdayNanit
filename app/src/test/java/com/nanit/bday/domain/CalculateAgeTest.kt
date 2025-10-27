package com.nanit.bday.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.Date

/**
 * Unit tests for the calculateAge function.
 * Tests various age calculation scenarios including edge cases.
 */
class CalculateAgeTest {

    private fun createDate(year: Int, month: Int, day: Int): Date {
        return Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }

    @Test
    fun `calculateAge for 6 month old baby returns months`() {
        val sixMonthsAgo = Calendar.getInstance().apply {
            add(Calendar.MONTH, -6)
        }.time

        val age = calculateAge(sixMonthsAgo)

        assertEquals(AgeUnit.MONTHS, age.unit)
        assertEquals(6, age.value)
    }

    @Test
    fun `calculateAge for 1 year old returns 12 months`() {
        val oneYearAgo = Calendar.getInstance().apply {
            add(Calendar.YEAR, -1)
        }.time

        val age = calculateAge(oneYearAgo)

        assertEquals(AgeUnit.MONTHS, age.unit)
        assertEquals(12, age.value)
    }

    @Test
    fun `calculateAge for 23 months old returns months`() {
        val twentyThreeMonthsAgo = Calendar.getInstance().apply {
            add(Calendar.MONTH, -23)
        }.time

        val age = calculateAge(twentyThreeMonthsAgo)

        assertEquals(AgeUnit.MONTHS, age.unit)
        assertEquals(23, age.value)
    }

    @Test
    fun `calculateAge for 2 year old returns years`() {
        val twoYearsAgo = Calendar.getInstance().apply {
            add(Calendar.YEAR, -2)
        }.time

        val age = calculateAge(twoYearsAgo)

        assertEquals(AgeUnit.YEARS, age.unit)
        assertEquals(2, age.value)
    }

    @Test
    fun `calculateAge for 5 year old returns years`() {
        val fiveYearsAgo = Calendar.getInstance().apply {
            add(Calendar.YEAR, -5)
        }.time

        val age = calculateAge(fiveYearsAgo)

        assertEquals(AgeUnit.YEARS, age.unit)
        assertEquals(5, age.value)
    }

    @Test
    fun `calculateAge for 9 year old returns 9 years`() {
        val nineYearsAgo = Calendar.getInstance().apply {
            add(Calendar.YEAR, -9)
        }.time

        val age = calculateAge(nineYearsAgo)

        assertEquals(AgeUnit.YEARS, age.unit)
        assertEquals(9, age.value)
    }

    @Test
    fun `calculateAge for 12 year old is capped at 9 years`() {
        val twelveYearsAgo = Calendar.getInstance().apply {
            add(Calendar.YEAR, -12)
        }.time

        val age = calculateAge(twelveYearsAgo)

        assertEquals(AgeUnit.YEARS, age.unit)
        //according to the requirement, age should be capped at 9 years
        assertEquals(9, age.value)
    }

    @Test
    fun `calculateAge for 15 year old is capped at 9 years`() {
        val fifteenYearsAgo = Calendar.getInstance().apply {
            add(Calendar.YEAR, -15)
        }.time

        val age = calculateAge(fifteenYearsAgo)

        assertEquals(AgeUnit.YEARS, age.unit)
        assertEquals(9, age.value)
    }

    @Test
    fun `calculateAge for newborn returns 0 months`() {
        val today = Calendar.getInstance().time

        val age = calculateAge(today)

        assertEquals(AgeUnit.MONTHS, age.unit)
        assertEquals(0, age.value)
    }

    @Test
    fun `calculateAge handles birthday not yet happened this year`() {
        val today = Calendar.getInstance()
        val currentMonth = today.get(Calendar.MONTH)

        // Create a birth date 3 years ago but with a future birthday this year
        val birthDate = Calendar.getInstance().apply {
            add(Calendar.YEAR, -3)
            // Set birthday to next month
            set(Calendar.MONTH, (currentMonth + 1) % 12)
            set(Calendar.DAY_OF_MONTH, 15)
        }.time

        val age = calculateAge(birthDate)

        // Should be 2 years since birthday hasn't occurred yet this year
        assertEquals(AgeUnit.YEARS, age.unit)
        assertEquals(2, age.value)
    }

    @Test
    fun `calculateAge for over 9 years in months is capped at 108 months`() {
        val today = Calendar.getInstance()
        val tenYearsAgo = Calendar.getInstance().apply {
            add(Calendar.YEAR, -10)
        }.time

        val age = calculateAge(tenYearsAgo)

        // Should be capped at 9 years
        assertEquals(AgeUnit.YEARS, age.unit)
        assertEquals(9, age.value)
    }

    @Test
    fun `calculateAge handles exact birthday today for 1 year`() {
        val today = Calendar.getInstance()
        val exactlyOneYearAgo = Calendar.getInstance().apply {
            add(Calendar.YEAR, -1)
        }.time

        val age = calculateAge(exactlyOneYearAgo)

        assertEquals(AgeUnit.MONTHS, age.unit)
        assertEquals(12, age.value)
    }

    @Test
    fun `calculateAge for 18 months returns months`() {
        val eighteenMonthsAgo = Calendar.getInstance().apply {
            add(Calendar.MONTH, -18)
        }.time

        val age = calculateAge(eighteenMonthsAgo)

        assertEquals(AgeUnit.MONTHS, age.unit)
        assertEquals(18, age.value)
    }

    @Test
    fun `BirthdayData correctly calculates age from birth date`() {
        val birthDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, -8)
        }.time

        val birthdayData = BirthdayData(
            name = "Test Baby",
            bDate = birthDate,
            theme = BirthdayTheme.PELICAN
        )

        assertEquals(AgeUnit.MONTHS, birthdayData.age.unit)
        assertEquals(8, birthdayData.age.value)
    }

    @Test
    fun `Age is calculated only once in BirthdayData`() {
        val birthDate = Calendar.getInstance().apply {
            add(Calendar.YEAR, -3)
        }.time

        val birthdayData = BirthdayData(
            name = "Test Child",
            bDate = birthDate,
            theme = BirthdayTheme.FOX
        )

        // Access age multiple times - should be same instance
        val age1 = birthdayData.age
        val age2 = birthdayData.age

        assertEquals(age1, age2)
        assertEquals(AgeUnit.YEARS, age1.unit)
        assertEquals(3, age1.value)
    }
}
