package com.devspace.myapplication

data class SearchRecipeResponse(
    val results: List<SearchRecipeDto>
)

data class SearchRecipeDto(
    val id: Int,
    val title: String,
    val readyInMinutes: Int,
    val image: String
)