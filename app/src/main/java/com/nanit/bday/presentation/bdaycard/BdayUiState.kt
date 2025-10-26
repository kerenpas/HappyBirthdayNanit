package com.nanit.bday.presentation.bdaycard

import com.nanit.bday.domain.Age
import com.nanit.bday.domain.AgeUnit
import com.nanit.bday.domain.BirthdayData


sealed class BdayUiState {

    object Loading : BdayUiState()

    data class Error(
        val message: String
    ) : BdayUiState()

    data class Success(
        val name: String,
        val themeResources: ThemRes,
        val age: Age,
        val numberIconResource: Int,
        val ageText: String,
        val photoUri: String? = null
    ) : BdayUiState()
}