package com.devspace.myapplication.main.data

import com.devspace.myapplication.RecipeResponse
import retrofit2.Call
import retrofit2.http.GET

interface RecipeRandom {
    @GET("recipes/random?number=20")
    fun getRandomRecipes() : Call<RecipeResponse>
}