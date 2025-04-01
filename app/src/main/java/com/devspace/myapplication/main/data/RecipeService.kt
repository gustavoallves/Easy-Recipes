package com.devspace.myapplication.main.data

import com.devspace.myapplication.common.RecipeDto
import com.devspace.myapplication.common.RecipeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeService {
    @GET("recipes/{id}/information")
    suspend fun getRecipeId(@Path("id") id: String): Response<RecipeDto>

    @GET("recipes/random?number=20")
    suspend fun getRandomRecipes() : Response<RecipeResponse>
}