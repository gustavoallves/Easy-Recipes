package com.devspace.myapplication.main.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.devspace.myapplication.common.RecipeDto
import com.devspace.myapplication.common.RetrofitClient
import com.devspace.myapplication.main.data.RecipeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MainScreenViewModel(
    private val recipeService: RecipeService
) : ViewModel() {

    private var _uiPopularRecipes = MutableStateFlow<List<RecipeDto>>(emptyList())
    val uiPopularRecipes: StateFlow<List<RecipeDto>> = _uiPopularRecipes

    private var _uiRandomRecipes = MutableStateFlow<List<RecipeDto>>(emptyList())
    val uiRandomRecipes: StateFlow<List<RecipeDto>> = _uiRandomRecipes

    init {
        fetchPopularRecipes()
        fetchGetRandomRecipes()
    }

    private fun fetchPopularRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            val recipesID = listOf(650855, 637932, 664636)
            val fetchedRecipes = mutableListOf<RecipeDto>()
            recipesID.forEach { id ->
                val response = recipeService.getRecipeId(id.toString())
                if (response.isSuccessful) {
                    response.body()?.let { fetchedRecipes.add(it) }
                    if (fetchedRecipes.size == recipesID.size) {
                        _uiPopularRecipes.value = fetchedRecipes.toList()
                    }
                } else {
                    Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
                }
            }
        }
    }

    private fun fetchGetRandomRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = recipeService.getRandomRecipes()
            if (response.isSuccessful) {
                val recipes = response.body()?.recipes
                if (recipes != null) {
                    _uiRandomRecipes.value = recipes
                }
            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val recipeService =
                    RetrofitClient.retrofitInstance.create(RecipeService::class.java)
                return MainScreenViewModel(recipeService) as T
            }
        }
    }
}
