package com.devspace.myapplication.detail.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.devspace.myapplication.common.RecipeDto
import com.devspace.myapplication.common.RetrofitClient
import com.devspace.myapplication.detail.data.DetailService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RecipeDetailViewModel(
    private val detailService: DetailService
) : ViewModel() {

    private var _uiRecipeDetail = MutableStateFlow<RecipeDto?>(null)
    val uiRecipeDetail: StateFlow<RecipeDto?> = _uiRecipeDetail

    fun fetchRecipeDetail(recipeId: String) {
        if (_uiRecipeDetail.value == null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = detailService.getRecipeInfo(recipeId)
                    if (response.isSuccessful) {
                        _uiRecipeDetail.value = response.body()
                    } else {
                        Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    fun cleanRecipeId() {
        viewModelScope.launch {
            delay(1000)
            _uiRecipeDetail.value = null
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val detailService =
                    RetrofitClient.retrofitInstance.create(DetailService::class.java)
                return RecipeDetailViewModel(detailService) as T
            }

        }
    }
}
