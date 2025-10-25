package com.nanit.bday.presentation.bdaycard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BirthdayScreen(
    viewModel: BirthdayViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {

    Column {

        TextField(
            value = "Keren",
            onValueChange = {},
            label = { androidx.compose.material3.Text("Name") }
        )

        TextField(
            value = "happy birthday",
            onValueChange = {},
            label = { androidx.compose.material3.Text("Birthday") }
        )

    }
}