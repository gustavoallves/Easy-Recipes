package com.devspace.myapplication.search.presentation

import android.util.Log
    import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.devspace.myapplication.common.RecipeDto
import com.devspace.myapplication.common.RetrofitClient
import com.devspace.myapplication.search.data.SearchService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SearchRecipeViewModel(
    private val searchService: SearchService
) : ViewModel() {

    private var _uiSearchRecipe = MutableStateFlow<List<RecipeDto>>(emptyList())
    var uiSearchRecipe: StateFlow<List<RecipeDto>> = _uiSearchRecipe

    fun fetchSearchRecipe(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = searchService.searchRecipes(query)
            if (response.isSuccessful) {
                Log.d("API_SEARCH_RESPONSE", "API_RESPONSE: ${response.body()}")
                _uiSearchRecipe.value = response.body()?.results ?: emptyList()
            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val searchService =
                    RetrofitClient.retrofitInstance.create(SearchService::class.java)
                return SearchRecipeViewModel(searchService) as T
            }
        }
    }


}
