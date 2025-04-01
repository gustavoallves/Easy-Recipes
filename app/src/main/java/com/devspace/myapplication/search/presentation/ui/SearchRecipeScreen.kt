package com.devspace.myapplication.search.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.devspace.myapplication.R
import com.devspace.myapplication.common.RecipeDto
import com.devspace.myapplication.search.presentation.SearchRecipeViewModel
import com.devspace.myapplication.ui.theme.poppinsFontFamily

@Composable
fun SearchRecipeScreen(
    query: String, navHostController: NavHostController, searchRecipeViewModel: SearchRecipeViewModel
) {
    val searchRecipes by searchRecipeViewModel.uiSearchRecipe.collectAsState()
    searchRecipeViewModel.fetchSearchRecipe(query)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(MaterialTheme.colorScheme.primary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navHostController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Arrow Back button",
                    tint = Color.White
                )
            }
            Text(
                modifier = Modifier
                    .padding(start = 4.dp),
                text = query
            )
        }

        SearchRecipeContent(recipes = searchRecipes, onClick = { itemClicked ->
            navHostController.navigate(route = "recipe_detail/${itemClicked.id}")
        })
    }
}

@Composable
private fun SearchRecipeContent(
    recipes: List<RecipeDto>, onClick: (RecipeDto) -> Unit
) {
    SearchRecipeList(recipes, onClick)
}

@Composable
fun SearchRecipeList(
    recipes: List<RecipeDto>, onClick: (RecipeDto) -> Unit
) {
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
            SearchRecipeItem(
                recipe = it, onClick = onClick
            )
        }
    }
}

@Composable
private fun SearchRecipeItem(
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