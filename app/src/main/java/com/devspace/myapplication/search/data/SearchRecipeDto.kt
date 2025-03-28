package com.devspace.myapplication.search.data

data class SearchRecipeDto(
    val id: Int,
    val title: String,
    val readyInMinutes: Int,
    val image: String
)