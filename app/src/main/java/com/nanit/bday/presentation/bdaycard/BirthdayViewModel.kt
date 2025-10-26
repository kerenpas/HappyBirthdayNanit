package com.nanit.bday.presentation.bdaycard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanit.bday.domain.BirthdayData
import com.nanit.bday.domain.BirthdayTheme
import com.nanit.bday.domain.usecase.ObserveBirthdayDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    private val observeBirthdayDataUseCase: ObserveBirthdayDataUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BdayUiState())
    val uiState: StateFlow<BdayUiState> = _uiState.asStateFlow()

    init {
        observeCachedBirthdayData()
    }

    private fun observeCachedBirthdayData() {

        viewModelScope.launch {
            observeBirthdayDataUseCase.invoke().filterNotNull().collect{ birthdayData ->
                try {
                    Log.d("BirthdayViewModel", "Keren Received birthday data: $birthdayData")
                    processedBirthdayData(birthdayData = birthdayData)
                } catch (e: Exception) {
                    _uiState.update { it.copy(error = "Error processing birthday data: ${e.message}") }
                }

            }
        }

    }

    fun updatePhotoUri(uri: String) {
        _uiState.update { it.copy(photoUri = uri) }
    }

    private fun processedBirthdayData(birthdayData: BirthdayData) {
        val ageInfo =  birthdayData.age
        val themeResources = birthdayData.theme.toThemeResources()
        val name = birthdayData.name
        val numberIconResource = ageInfo.value.toNumberIconResource()
        val ageText = ageInfo.toDisplayText()


        _uiState.value = (
            BdayUiState(
                photoUri = null,
                name = name,
                numberIconResource = numberIconResource,
                ageText = ageText,
                themeResources = themeResources,
                error = null
            )
        )
    }
}