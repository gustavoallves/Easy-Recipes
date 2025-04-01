package com.devspace.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devspace.myapplication.detail.presentation.RecipeDetailViewModel
import com.devspace.myapplication.detail.presentation.ui.RecipeDetailScreen
import com.devspace.myapplication.main.presentation.MainScreenViewModel
import com.devspace.myapplication.main.presentation.ui.MainScreen
import com.devspace.myapplication.search.presentation.SearchRecipeViewModel
import com.devspace.myapplication.search.presentation.ui.SearchRecipeScreen
import com.devspace.myapplication.start.ui.StartScreen

@Composable
fun EasyRecipesApp(
    mainScreenViewModel: MainScreenViewModel,
    recipeDetailViewModel: RecipeDetailViewModel,
    searchRecipeViewModel: SearchRecipeViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "start_screen") {
        composable(route = "start_screen") {
            StartScreen(navController)
        }
        composable(route = "main_screen") {
            MainScreen(navController, mainScreenViewModel)
        }
        composable(
            route = "recipe_detail" + "/{itemId}",
            arguments = listOf(navArgument("itemId") {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val recipeId = requireNotNull(navBackStackEntry.arguments?.getString("itemId"))
            RecipeDetailScreen(recipeId, navController, recipeDetailViewModel)
        }
        composable(
            route = "search_recipe" + "/{query}",
            arguments = listOf(navArgument("query") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val id = requireNotNull(backStackEntry.arguments?.getString("query"))
            SearchRecipeScreen(id, navController, searchRecipeViewModel)
        }
    }
}