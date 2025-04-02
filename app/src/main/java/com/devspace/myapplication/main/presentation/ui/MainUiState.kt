package com.devspace.myapplication.main.presentation.ui

import java.lang.Error

data class MainUiState(
    val dataList: List<MainUiData> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = "Something went wrong"
)

data class MainUiData(
    val id: Int,
    val title: String,
    val summary: String,
    val readyInMinutes: Int,
    val image: String
)
