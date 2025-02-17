package com.devspace.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun App() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "start_screen") {
        composable(route = "start_screen") {
            StartScreen(navController)
        }
        composable(route = "main_screen") {
            MainScreen(navController)
        }
        composable(
            route = "recipe_detail" + "/{itemId}",
            arguments = listOf(navArgument("itemId") {
                type = NavType.StringType
            })
        ) {
            navBackStackEntry ->
                val id = requireNotNull(navBackStackEntry.arguments?.getString("itemId"))
                RecipeDetailScreen(id, navController)
        }
        composable(
            route = "search_recipe" + "/{query}",
            arguments = listOf(navArgument("query"){
                type = NavType.StringType
            })
        ){ backStackEntry ->
            val id = requireNotNull(backStackEntry.arguments?.getString("query"))
            SearchRecipeScreen(id, navController)
        }
    }
}