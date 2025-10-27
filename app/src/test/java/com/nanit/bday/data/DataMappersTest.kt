package com.nanit.bday.data

import com.nanit.bday.data.dto.BirthdayDto
import com.nanit.bday.domain.BirthdayTheme
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

/**
 * Unit tests for data layer mapper functions.
 * Tests mapping from DTOs to domain models.
 */
class DataMappersTest {

    @Test
    fun `ConnectionState Idle maps to domain Idle`() {
        val dataState = com.nanit.bday.data.dto.ConnectionState.Idle
        val domainState = dataState.toDomainModel()

        assertEquals(com.nanit.bday.domain.ConnectionState.Idle, domainState)
    }

    @Test
    fun `ConnectionState Connecting maps to domain Connecting`() {
        val dataState = com.nanit.bday.data.dto.ConnectionState.Connecting
        val domainState = dataState.toDomainModel()

        assertEquals(com.nanit.bday.domain.ConnectionState.Connecting, domainState)
    }

    @Test
    fun `ConnectionState Connected maps to domain Connected`() {
        val dataState = com.nanit.bday.data.dto.ConnectionState.Connected
        val domainState = dataState.toDomainModel()

        assertEquals(com.nanit.bday.domain.ConnectionState.Connected, domainState)
    }

    @Test
    fun `ConnectionState Error maps to domain Error with message`() {
        val errorMessage = "Connection failed"
        val dataState = com.nanit.bday.data.dto.ConnectionState.Error(errorMessage)
        val domainState = dataState.toDomainModel()

        assert(domainState is com.nanit.bday.domain.ConnectionState.Error)
        assertEquals(errorMessage, (domainState as com.nanit.bday.domain.ConnectionState.Error).message)
    }

    @Test
    fun `ConnectionState Disconnected maps to domain Error with Disconnected message`() {
        val dataState = com.nanit.bday.data.dto.ConnectionState.Disconnected
        val domainState = dataState.toDomainModel()

        assert(domainState is com.nanit.bday.domain.ConnectionState.Error)
        assertEquals("Disconnected", (domainState as com.nanit.bday.domain.ConnectionState.Error).message)
    }

    @Test
    fun `BirthdayDto toDomain maps all fields correctly`() {
        val timestamp = 1609459200000L // Jan 1, 2021, 00:00:00 UTC
        val dto = BirthdayDto(
            name = "Alice",
            dob = timestamp,
            theme = "pelican"
        )

        val domain = dto.toDomain()

        assertEquals("Alice", domain.name)
        assertEquals(Date(timestamp), domain.bDate)
        assertEquals(BirthdayTheme.PELICAN, domain.theme)
    }

    @Test
    fun `BirthdayDto with pelican theme maps correctly`() {
        val dto = BirthdayDto(
            name = "Bob",
            dob = 1609459200000L,
            theme = "pelican"
        )

        val domain = dto.toDomain()

        assertEquals(BirthdayTheme.PELICAN, domain.theme)
    }

    @Test
    fun `BirthdayDto with fox theme maps correctly`() {
        val dto = BirthdayDto(
            name = "Charlie",
            dob = 1609459200000L,
            theme = "fox"
        )

        val domain = dto.toDomain()

        assertEquals(BirthdayTheme.FOX, domain.theme)
    }

    @Test
    fun `BirthdayDto with elephant theme maps correctly`() {
        val dto = BirthdayDto(
            name = "Diana",
            dob = 1609459200000L,
            theme = "elephant"
        )

        val domain = dto.toDomain()

        assertEquals(BirthdayTheme.ELEPHANT, domain.theme)
    }

    @Test
    fun `BirthdayDto with uppercase theme maps correctly`() {
        val dto = BirthdayDto(
            name = "Eve",
            dob = 1609459200000L,
            theme = "PELICAN"
        )

        val domain = dto.toDomain()

        assertEquals(BirthdayTheme.PELICAN, domain.theme)
    }

    @Test
    fun `BirthdayDto with mixed case theme maps correctly`() {
        val dto = BirthdayDto(
            name = "Frank",
            dob = 1609459200000L,
            theme = "FoX"
        )

        val domain = dto.toDomain()

        assertEquals(BirthdayTheme.FOX, domain.theme)
    }

    @Test
    fun `BirthdayDto with unknown theme defaults to PELICAN`() {
        val dto = BirthdayDto(
            name = "Grace",
            dob = 1609459200000L,
            theme = "unknown"
        )

        val domain = dto.toDomain()

        assertEquals(BirthdayTheme.PELICAN, domain.theme)
    }

    @Test
    fun `BirthdayDto with empty theme defaults to PELICAN`() {
        val dto = BirthdayDto(
            name = "Henry",
            dob = 1609459200000L,
            theme = ""
        )

        val domain = dto.toDomain()

        assertEquals(BirthdayTheme.PELICAN, domain.theme)
    }

    @Test
    fun `BirthdayTheme fromString handles all valid themes`() {
        assertEquals(BirthdayTheme.PELICAN, BirthdayTheme.fromString("pelican"))
        assertEquals(BirthdayTheme.FOX, BirthdayTheme.fromString("fox"))
        assertEquals(BirthdayTheme.ELEPHANT, BirthdayTheme.fromString("elephant"))
    }

    @Test
    fun `BirthdayTheme fromString is case insensitive`() {
        assertEquals(BirthdayTheme.PELICAN, BirthdayTheme.fromString("PELICAN"))
        assertEquals(BirthdayTheme.FOX, BirthdayTheme.fromString("Fox"))
        assertEquals(BirthdayTheme.ELEPHANT, BirthdayTheme.fromString("ElEpHaNt"))
    }

    @Test
    fun `BirthdayTheme fromString handles invalid input`() {
        assertEquals(BirthdayTheme.PELICAN, BirthdayTheme.fromString("invalid"))
        assertEquals(BirthdayTheme.PELICAN, BirthdayTheme.fromString(""))
        assertEquals(BirthdayTheme.PELICAN, BirthdayTheme.fromString("bear"))
    }
}
