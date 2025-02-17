package com.devspace.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("recipes/random?number=20")
    fun getRandomRecipes() : Call<RecipeResponse>

    @GET("recipes/{id}/information?includeNutrition=false")
    fun getRecipeInfo(@Path("id") id: String): Call<RecipeDto>

    @GET("/recipes/complexSearch?")
    fun searchRecipes(@Query("query") query: String): Call<SearchRecipeResponse>

}