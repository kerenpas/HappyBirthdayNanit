package com.nanit.bday.presentation.bdaycard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BirthdayScreen(
    viewModel: BirthdayViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column {

        TextField(
            value = uiState.name,
            onValueChange = {},
            label = { androidx.compose.material3.Text("Name") }
        )

        TextField(
            value = uiState.ageText,
            onValueChange = {},
            label = { androidx.compose.material3.Text("Birthday") }
        )

    }
}