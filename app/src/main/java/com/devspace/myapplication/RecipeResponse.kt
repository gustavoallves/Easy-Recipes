package com.devspace.myapplication

data class RecipeResponse(
    val recipes: List<RecipeDto>
)

data class RecipeDto (
    val id: Int,
    val title: String,
    val summary: String,
    val readyInMinutes: Int,
    val image: String
    )