package com.devspace.myapplication.search.data

import com.devspace.myapplication.SearchRecipeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeSearch {
    @GET("/recipes/complexSearch?")
    fun searchRecipes(@Query("query") query: String): Call<SearchRecipeResponse>
}