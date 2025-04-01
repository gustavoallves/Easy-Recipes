package com.devspace.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.devspace.myapplication.detail.presentation.RecipeDetailViewModel
import com.devspace.myapplication.main.presentation.MainScreenViewModel
import com.devspace.myapplication.search.presentation.SearchRecipeViewModel
import com.devspace.myapplication.ui.theme.EasyRecipesTheme

class MainActivity : ComponentActivity() {

    private val mainScreenViewModel by viewModels<MainScreenViewModel> { MainScreenViewModel.Factory }
    private val recipeDetailViewModel by viewModels<RecipeDetailViewModel> { RecipeDetailViewModel.Factory }
    private val searchRecipeViewModel by viewModels<SearchRecipeViewModel> { SearchRecipeViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyRecipesTheme {
                Surface(
                    modifier = Modifier
                        .safeDrawingPadding()
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EasyRecipesApp(
                        mainScreenViewModel = mainScreenViewModel,
                        recipeDetailViewModel = recipeDetailViewModel,
                        searchRecipeViewModel = searchRecipeViewModel
                    )
                }
            }
        }
    }
}