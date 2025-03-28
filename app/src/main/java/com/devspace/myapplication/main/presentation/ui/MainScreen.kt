package com.devspace.myapplication.main.presentation.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.devspace.myapplication.APIService
import com.devspace.myapplication.R
import com.devspace.myapplication.RecipeDto
import com.devspace.myapplication.RecipeResponse
import com.devspace.myapplication.search.presentation.ui.components.SearchBarUI
import com.devspace.myapplication.common.RetrofitClient
import com.devspace.myapplication.ui.theme.EasyRecipesTheme
import com.devspace.myapplication.ui.theme.poppinsFontFamily
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MainScreen(navHostController: NavHostController) {

    var recipes by rememberSaveable { mutableStateOf<List<RecipeDto>>(emptyList()) }
    var popularRecipes by rememberSaveable { mutableStateOf<List<RecipeDto>>(emptyList()) }
    val retrofit = RetrofitClient.retrofitInstance.create(APIService::class.java)

    if (recipes.isEmpty()) {
        retrofit.getRandomRecipes().enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>, response: Response<RecipeResponse>
            ) {
                if (response.isSuccessful) {
                    recipes = response.body()?.recipes ?: emptyList()
                } else {
                    Log.d("MainAMainActivity", "Request Error :: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.d("MainActivity", "Network Error :: ${t.message}")
            }

        })
    }

    val recipesID = listOf(650855, 637932, 664636)
    LaunchedEffect(key1 = true) {
        val fetchedRecipes = mutableListOf<RecipeDto>()
        recipesID.forEach { id ->
            retrofit.getRecipeInfo(id.toString()).enqueue(object : Callback<RecipeDto> {
                override fun onResponse(call: Call<RecipeDto>, response: Response<RecipeDto>) {
                    if (response.isSuccessful) {
                        response.body()?.let { fetchedRecipes.add(it) }
                        if (fetchedRecipes.size == recipesID.size) {
                            popularRecipes = fetchedRecipes.toList()
                        }
                    } else {
                        Log.d("MainAMainActivity", "Request Error :: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<RecipeDto>, t: Throwable) {
                    Log.d("MainActivity", "Network Error :: ${t.message}")
                }
            })
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        MainContent(
            recipes = recipes,
            onSearchClicked = { query ->
                val tempCleanQuery = query.trim()
                if (tempCleanQuery.isNotEmpty()) {
                    navHostController.navigate(route = "search_recipe/$tempCleanQuery")
                }
            },
            onClick = { itemClicked ->
                navHostController.navigate(route = "recipe_detail/${itemClicked.id}")
            },
            popularRecipes = popularRecipes
        )
    }
}

@Composable
fun MainContent(
    popularRecipes: List<RecipeDto>,
    recipes: List<RecipeDto>,
    onClick: (RecipeDto) -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        var query by remember { mutableStateOf("") }

        PopularSession(
            label0 = "Discover your\nfavorite recipes!",
            label1 = "Popular recipes",
            recipes = popularRecipes,
            onClick = onClick
        )
        SearchSession(
            query = query, onQueryChange = { newQuery ->
                query = newQuery
            },
            onSearchClicked = onSearchClicked
        )
        RecipeSession(
            label = "All recipes", recipes = recipes, onClick = onClick
        )
    }
}

@Composable
fun PopularSession(
    label0: String, label1: String, recipes: List<RecipeDto>, onClick: (RecipeDto) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            fontFamily = poppinsFontFamily,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            text = label0
        )
        Text(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            fontSize = 20.sp,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            text = label1
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(start = 20.dp, top = 5.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            recipes.forEach { recipe ->
                RecipeItem(
                    recipe = recipe, onClick = onClick
                )
            }
        }
    }

}

@Composable
fun SearchSession(
    query: String, onQueryChange: (String) -> Unit, onSearchClicked: (String) -> Unit
) {
    SearchBarUI(
        query = query,
        onQueryChange = onQueryChange,
        placeholder = "Search...",
        onSearchClicked = { onSearchClicked.invoke(query) },
    )
}

@Composable
fun RecipeSession(
    label: String, recipes: List<RecipeDto>, onClick: (RecipeDto) -> Unit
) {
    Text(
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp),
        fontSize = 20.sp,
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.Medium,
        text = label
    )
    RecipeList(
        recipes = recipes, onClick = onClick
    )
}

@Composable
private fun RecipeList(recipes: List<RecipeDto>, onClick: (RecipeDto) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(1500.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = false
    ) {
        items(recipes) {
            RecipeItem(
                recipe = it, onClick = onClick
            )
        }
    }
}

@Composable
fun RecipeItem(
    recipe: RecipeDto, onClick: (RecipeDto) -> Unit
) {
    Box(modifier = Modifier
        .width(180.dp)
        .height(170.dp)
        .clip(shape = RoundedCornerShape(12.dp))
        .clickable {
            onClick.invoke(recipe)
        }) {

        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            model = recipe.image,
            contentDescription = "${recipe.title} Image"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.Black.copy(alpha = 0.5f))
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = recipe.title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(10.dp),
                        painter = painterResource(R.drawable.ic_clock_time),
                        contentDescription = "Clock Icon"
                    )
                    Text(
                        text = recipe.readyInMinutes.toString() + " min",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    EasyRecipesTheme {
        val dummyRecipes = listOf(
            RecipeDto(
                1,
                "Recipe 1",
                "description here",
                30,
                "https://spoonacular.com/recipeImages/634028-556x370.jpg"
            ), RecipeDto(
                2,
                "Recipe 2",
                "description here",
                30,
                "https://spoonacular.com/recipeImages/634028-556x370.jpg"
            ), RecipeDto(
                3,
                "Recipe 3",
                "description here",
                30,
                "https://spoonacular.com/recipeImages/634028-556x370.jpg"
            ), RecipeDto(
                4,
                "Recipe 4",
                "description here",
                30,
                "https://spoonacular.com/recipeImages/634028-556x370.jpg"
            ), RecipeDto(
                5,
                "Recipe 5",
                "description here",
                30,
                "https://spoonacular.com/recipeImages/634028-556x370.jpg"
            ), RecipeDto(
                6,
                "Recipe 6",
                "description here",
                30,
                "https://spoonacular.com/recipeImages/634028-556x370.jpg"
            )
        )
        PopularSession(
            label0 = "Discover your\nfavorite recipes!",
            label1 = "Popular recipes",
            recipes = dummyRecipes
        ) {

        }
    }
}