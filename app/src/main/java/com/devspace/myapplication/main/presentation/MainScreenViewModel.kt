package com.devspace.myapplication.main.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.devspace.myapplication.common.RecipeDto
import com.devspace.myapplication.common.RetrofitClient
import com.devspace.myapplication.main.data.RecipeService
import com.devspace.myapplication.main.presentation.ui.MainUiData
import com.devspace.myapplication.main.presentation.ui.MainUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

data class MainScreenViewModel(
    private val recipeService: RecipeService
) : ViewModel() {

    private var _uiPopularRecipes = MutableStateFlow<List<RecipeDto>>(emptyList())
    val uiPopularRecipes: StateFlow<List<RecipeDto>> = _uiPopularRecipes

    private var _uiRandomRecipes = MutableStateFlow(MainUiState())
    val uiRandomRecipes: StateFlow<MainUiState> = _uiRandomRecipes

    init {
        fetchPopularRecipes()
        fetchGetRandomRecipes()
    }

    private fun fetchPopularRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            val recipesID = listOf(650855, 637932, 664636)
            val fetchedRecipes = mutableListOf<RecipeDto>()
            recipesID.forEach { id ->
                try {
                    val response = recipeService.getRecipeId(id.toString())
                    if (response.isSuccessful) {
                        response.body()?.let { fetchedRecipes.add(it) }
                        if (fetchedRecipes.size == recipesID.size) {
                            _uiPopularRecipes.value = fetchedRecipes.toList()
                        }
                    } else {
                        Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun fetchGetRandomRecipes() {
        _uiRandomRecipes.value = MainUiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = recipeService.getRandomRecipes()
                if (response.isSuccessful) {
                    val recipes = response.body()?.recipes
                    if (recipes != null) {
                        val recipeUiDataList = recipes.map { recipeDto ->
                            MainUiData(
                                id = recipeDto.id,
                                title = recipeDto.title,
                                summary = recipeDto.summary,
                                readyInMinutes = recipeDto.readyInMinutes,
                                image = recipeDto.image
                            )
                        }
                        _uiRandomRecipes.value = MainUiState(dataList = recipeUiDataList)
                    }
                } else {
                    _uiRandomRecipes.value = MainUiState(isError = true)
                    Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                if (ex is UnknownHostException){
                    _uiRandomRecipes.value = MainUiState(
                        isError = true,
                        errorMessage = "No internet connection"
                    )
                } else {
                    _uiRandomRecipes.value = MainUiState(isError = true)
                }
            }
        }
    }

    companion object {
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
