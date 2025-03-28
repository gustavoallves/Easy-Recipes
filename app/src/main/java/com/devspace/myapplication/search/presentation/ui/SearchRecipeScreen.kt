package com.devspace.myapplication.search.presentation.ui

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.devspace.myapplication.APIService
import com.devspace.myapplication.R
import com.devspace.myapplication.SearchRecipeDto
import com.devspace.myapplication.SearchRecipeResponse
import com.devspace.myapplication.common.RetrofitClient
import com.devspace.myapplication.ui.theme.poppinsFontFamily
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun SearchRecipeScreen(
    query: String, navHostController: NavHostController
) {
    val service = RetrofitClient.retrofitInstance.create(APIService::class.java)
    var searchRecipes by rememberSaveable { mutableStateOf<List<SearchRecipeDto>>(emptyList()) }

    if (searchRecipes.isEmpty()) {
        service.searchRecipes(query).enqueue(object : Callback<SearchRecipeResponse> {
            override fun onResponse(
                call: Call<SearchRecipeResponse>, response: Response<SearchRecipeResponse>
            ) {
                if (response.isSuccessful) {
                    searchRecipes = response.body()?.results ?: emptyList()
                } else {
                    Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<SearchRecipeResponse>, t: Throwable) {
                Log.d("MainActivity", "Network Error :: ${t.message}")
            }
        })
    }

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
    recipes: List<SearchRecipeDto>, onClick: (SearchRecipeDto) -> Unit
) {
    SearchRecipeList(recipes, onClick)
}

@Composable
fun SearchRecipeList(
    recipes: List<SearchRecipeDto>, onClick: (SearchRecipeDto) -> Unit
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
                searchRecipeDto = it, onClick = onClick
            )
        }
    }
}

@Composable
private fun SearchRecipeItem(
    searchRecipeDto: SearchRecipeDto, onClick: (SearchRecipeDto) -> Unit
) {
    Box(modifier = Modifier
        .width(180.dp)
        .height(170.dp)
        .clip(shape = RoundedCornerShape(12.dp))
        .clickable {
            onClick.invoke(searchRecipeDto)
        }) {

        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            model = searchRecipeDto.image,
            contentDescription = "${searchRecipeDto.title} Image"
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
                    text = searchRecipeDto.title,
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
                        text = searchRecipeDto.readyInMinutes.toString() + " min",
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