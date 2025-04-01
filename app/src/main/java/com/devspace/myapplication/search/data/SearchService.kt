package com.devspace.myapplication.search.data

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("/recipes/complexSearch?addRecipeInformation=true")
    suspend fun searchRecipes(@Query("query") query: String): Response<SearchRecipeResponse>
}