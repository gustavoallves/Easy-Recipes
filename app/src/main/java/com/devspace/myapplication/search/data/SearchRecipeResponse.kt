package com.devspace.myapplication.search.data

import com.devspace.myapplication.common.RecipeDto


data class SearchRecipeResponse(
    val results: List<RecipeDto>
)