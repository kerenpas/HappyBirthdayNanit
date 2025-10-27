package com.nanit.bday.presentation.bdaycard

import com.nanit.bday.R
import com.nanit.bday.domain.Age
import com.nanit.bday.domain.AgeUnit
import com.nanit.bday.domain.BirthdayTheme
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for presentation layer mapper functions.
 * Tests mapping from domain models to UI resources.
 */
class PresentationMappersTest {

    // BirthdayTheme.toThemeResources() tests

    @Test
    fun `PELICAN theme maps to correct blue resources`() {
        val themeRes = BirthdayTheme.PELICAN.toThemeResources()

        assertEquals(R.drawable.bg_android_pelican, themeRes.backgroundDrawable)
        assertEquals(R.color.background_blue, themeRes.backgroundColor)
        assertEquals(R.drawable.ic_baby_face_circle_blue_bordered, themeRes.borderedIcon)
        assertEquals(R.drawable.ic_baby_face_circle_blue_filled, themeRes.filledIcon)
        assertEquals(R.drawable.ic_baby_face_blue_small, themeRes.smallIcon)
        assertEquals(R.drawable.ic_camera_blue, themeRes.cameraIcon)
    }

    @Test
    fun `FOX theme maps to correct green resources`() {
        val themeRes = BirthdayTheme.FOX.toThemeResources()

        assertEquals(R.drawable.bg_fox, themeRes.backgroundDrawable)
        assertEquals(R.color.background_green, themeRes.backgroundColor)
        assertEquals(R.drawable.ic_baby_face_circle_green_bordered, themeRes.borderedIcon)
        assertEquals(R.drawable.ic_baby_face_circle_green_filled, themeRes.filledIcon)
        assertEquals(R.drawable.ic_baby_face_green_small, themeRes.smallIcon)
        assertEquals(R.drawable.ic_camera_green, themeRes.cameraIcon)
    }

    @Test
    fun `ELEPHANT theme maps to correct yellow resources`() {
        val themeRes = BirthdayTheme.ELEPHANT.toThemeResources()

        assertEquals(R.drawable.bg_android_elephant, themeRes.backgroundDrawable)
        assertEquals(R.color.background_yellow, themeRes.backgroundColor)
        assertEquals(R.drawable.ic_baby_face_circle_yellow_bordered, themeRes.borderedIcon)
        assertEquals(R.drawable.ic_baby_face_circle_yellow_filled, themeRes.filledIcon)
        assertEquals(R.drawable.ic_baby_face_yellow_small, themeRes.smallIcon)
        assertEquals(R.drawable.ic_camera_yellow, themeRes.cameraIcon)
    }

    // Int.toNumberIconResource() tests

    @Test
    fun `number 0 maps to correct icon`() {
        assertEquals(R.drawable.ic_number_00, 0.toNumberIconResource())
    }

    @Test
    fun `number 1 maps to correct icon`() {
        assertEquals(R.drawable.ic_number_01, 1.toNumberIconResource())
    }

    @Test
    fun `number 2 maps to correct icon`() {
        assertEquals(R.drawable.ic_number_02, 2.toNumberIconResource())
    }

    @Test
    fun `number 3 maps to correct icon`() {
        assertEquals(R.drawable.ic_number_03, 3.toNumberIconResource())
    }

    @Test
    fun `number 4 maps to correct icon`() {
        assertEquals(R.drawable.ic_number_04, 4.toNumberIconResource())
    }

    @Test
    fun `number 5 maps to correct icon`() {
        assertEquals(R.drawable.ic_number_05, 5.toNumberIconResource())
    }

    @Test
    fun `number 6 maps to correct icon`() {
        assertEquals(R.drawable.ic_number_06, 6.toNumberIconResource())
    }

    @Test
    fun `number 7 maps to correct icon`() {
        assertEquals(R.drawable.ic_number_07, 7.toNumberIconResource())
    }

    @Test
    fun `number 8 maps to correct icon`() {
        assertEquals(R.drawable.ic_number_08, 8.toNumberIconResource())
    }

    @Test
    fun `number 9 maps to correct icon`() {
        assertEquals(R.drawable.ic_number_09, 9.toNumberIconResource())
    }

    @Test
    fun `number 10 maps to correct icon`() {
        assertEquals(R.drawable.ic_number_10, 10.toNumberIconResource())
    }

    @Test
    fun `number 11 maps to correct icon`() {
        assertEquals(R.drawable.ic_number_11, 11.toNumberIconResource())
    }

    @Test
    fun `number 12 maps to correct icon`() {
        assertEquals(R.drawable.ic_number_12, 12.toNumberIconResource())
    }

    @Test
    fun `number greater than 12 defaults to 00 icon`() {
        assertEquals(R.drawable.ic_number_00, 13.toNumberIconResource())
        assertEquals(R.drawable.ic_number_00, 100.toNumberIconResource())
        assertEquals(R.drawable.ic_number_00, 999.toNumberIconResource())
    }

    @Test
    fun `negative number defaults to 00 icon`() {
        assertEquals(R.drawable.ic_number_00, (-1).toNumberIconResource())
        assertEquals(R.drawable.ic_number_00, (-10).toNumberIconResource())
    }

    // Age.toDisplayText() tests

    @Test
    fun `1 month displays as MONTH OLD singular`() {
        val age = Age(value = 1, unit = AgeUnit.MONTHS)
        assertEquals("MONTH OLD!", age.toDisplayText())
    }

    @Test
    fun `0 months displays as MONTHS OLD plural`() {
        val age = Age(value = 0, unit = AgeUnit.MONTHS)
        assertEquals("MONTHS OLD!", age.toDisplayText())
    }

    @Test
    fun `2 months displays as MONTHS OLD plural`() {
        val age = Age(value = 2, unit = AgeUnit.MONTHS)
        assertEquals("MONTHS OLD!", age.toDisplayText())
    }

    @Test
    fun `6 months displays as MONTHS OLD plural`() {
        val age = Age(value = 6, unit = AgeUnit.MONTHS)
        assertEquals("MONTHS OLD!", age.toDisplayText())
    }

    @Test
    fun `12 months displays as MONTHS OLD plural`() {
        val age = Age(value = 12, unit = AgeUnit.MONTHS)
        assertEquals("MONTHS OLD!", age.toDisplayText())
    }

    @Test
    fun `1 year displays as YEAR OLD singular`() {
        val age = Age(value = 1, unit = AgeUnit.YEARS)
        assertEquals("YEAR OLD!", age.toDisplayText())
    }

    @Test
    fun `2 years displays as YEARS OLD plural`() {
        val age = Age(value = 2, unit = AgeUnit.YEARS)
        assertEquals("YEARS OLD!", age.toDisplayText())
    }

    @Test
    fun `5 years displays as YEARS OLD plural`() {
        val age = Age(value = 5, unit = AgeUnit.YEARS)
        assertEquals("YEARS OLD!", age.toDisplayText())
    }

    @Test
    fun `9 years displays as YEARS OLD plural`() {
        val age = Age(value = 9, unit = AgeUnit.YEARS)
        assertEquals("YEARS OLD!", age.toDisplayText())
    }

    @Test
    fun `0 years displays as YEARS OLD plural`() {
        val age = Age(value = 0, unit = AgeUnit.YEARS)
        assertEquals("YEARS OLD!", age.toDisplayText())
    }

    // Integration tests for complete mapping chain

    @Test
    fun `complete mapping for 8 month old baby`() {
        val age = Age(value = 8, unit = AgeUnit.MONTHS)
        val displayText = age.toDisplayText()
        val numberIcon = age.value.toNumberIconResource()

        assertEquals("MONTHS OLD!", displayText)
        assertEquals(R.drawable.ic_number_08, numberIcon)
    }

    @Test
    fun `complete mapping for 3 year old child with FOX theme`() {
        val age = Age(value = 3, unit = AgeUnit.YEARS)
        val displayText = age.toDisplayText()
        val numberIcon = age.value.toNumberIconResource()
        val themeRes = BirthdayTheme.FOX.toThemeResources()

        assertEquals("YEARS OLD!", displayText)
        assertEquals(R.drawable.ic_number_03, numberIcon)
        assertEquals(R.color.background_green, themeRes.backgroundColor)
    }
}
