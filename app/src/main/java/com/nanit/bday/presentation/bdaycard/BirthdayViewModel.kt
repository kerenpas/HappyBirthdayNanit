package com.nanit.bday.presentation.bdaycard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanit.bday.domain.BirthdayData
import com.nanit.bday.domain.usecase.ObserveBirthdayDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    private val observeBirthdayDataUseCase: ObserveBirthdayDataUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<BdayUiState>(BdayUiState.Loading)
    val uiState: StateFlow<BdayUiState> = _uiState.asStateFlow()

    init {
        observeCachedBirthdayData()
    }

    private fun observeCachedBirthdayData() {

        viewModelScope.launch {
            observeBirthdayDataUseCase.invoke().filterNotNull().first().let { birthdayData ->
                try {
                    processedBirthdayData(birthdayData = birthdayData)
                } catch (e: Exception) {
                    _uiState.value = BdayUiState.Error(
                        message = "Error processing birthday data: ${e.message}"
                    )
                }

            }
        }

    }

    fun updatePhotoUri(uri: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is BdayUiState.Success -> currentState.copy(photoUri = uri)
                else -> currentState
            }
        }
    }

    private fun processedBirthdayData(birthdayData: BirthdayData) {
        val ageInfo =  birthdayData.age
        val themeResources = birthdayData.theme.toThemeResources()

        val numberIconResource = ageInfo.value.toNumberIconResource()
        val ageText = ageInfo.toDisplayText()

        val currentPhotoUri = when (val currentState = _uiState.value) {
            is BdayUiState.Success -> currentState.photoUri
            else -> null
        }

        _uiState.value = BdayUiState.Success(
            name = birthdayData.name,
            themeResources = themeResources,
            age = ageInfo,
            numberIconResource = numberIconResource,
            ageText = ageText,
            photoUri = currentPhotoUri
        )
    }
}