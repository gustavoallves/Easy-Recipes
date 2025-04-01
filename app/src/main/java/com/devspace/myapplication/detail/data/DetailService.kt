package com.devspace.myapplication.detail.data

import com.devspace.myapplication.common.RecipeDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailService {
    @GET("recipes/{id}/information?includeNutrition=false")
    suspend fun getRecipeInfo(@Path("id") recipeId: String): Response<RecipeDto>
}