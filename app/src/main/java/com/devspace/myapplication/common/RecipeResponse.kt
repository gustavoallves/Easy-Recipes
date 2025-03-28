package com.devspace.myapplication.common

import com.devspace.myapplication.RecipeDto

data class RecipeResponse(
    val recipes: List<RecipeDto>
)