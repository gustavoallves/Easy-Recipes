package com.devspace.myapplication.common

data class RecipeDto(
    val id: Int,
    val title: String,
    val summary: String,
    val readyInMinutes: Int,
    val image: String
)