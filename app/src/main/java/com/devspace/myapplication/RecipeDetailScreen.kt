package com.devspace.myapplication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.devspace.myapplication.ui.theme.poppinsFontFamily
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RecipeDetailScreen(id: String, navHostController: NavHostController) {

    val service = RetrofitClient.retrofitInstance.create(APIService::class.java)
    var recipeDto by remember { mutableStateOf<RecipeDto?>(null) }

    service.getRecipeInfo(id).enqueue(object : Callback<RecipeDto> {
        override fun onResponse(call: Call<RecipeDto>, response: Response<RecipeDto>) {
            if (response.isSuccessful) {
                recipeDto = response.body()
            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<RecipeDto>, t: Throwable) {
            Log.d("MainActivity", "Network Error :: ${t.message}")
        }
    })
    recipeDto?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row (
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {navHostController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Arrow Back Button",
                        tint = Color.White
                    )
                }
            }

            AsyncImage(
                modifier = Modifier
                    .height(305.dp),
                contentScale = ContentScale.Crop,
                model = it.image,
                contentDescription = "${it.title} Image"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily,
                        color = MaterialTheme.colorScheme.primary,
                        text = it.title
                    )
                    Spacer(
                        modifier = Modifier
                            .height(5.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(7.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                            .height(25.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier.size(15.dp),
                            painter = painterResource(R.drawable.ic_clock_time),
                            contentDescription = "Clock Icon",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = poppinsFontFamily,
                            color = MaterialTheme.colorScheme.primary,
                            text = it.readyInMinutes.toString() + " minutes"
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                    )
                    Text(
                        text = "Directions:",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily
                    )
                    RecipeDetailContent(it)
                }
            }
        }
    }
}

@Composable
fun RecipeDetailContent(recipe: RecipeDto) {
    HtmlTextUI(
        modifier = Modifier,
        text = recipe.summary
    )
}

//@Preview
//@Composable
//private fun RecipeDetailPreview() {
//    EasyRecipesTheme {
//        val dummyRecipes =
//            RecipeDto(
//                1,
//                "Recipe 1",
//                "We make it easy to add a recipe search to your app or website - and not just any recipe search. Our semantic search is fast, accurate, and pretty darn smart. \"Gluten free nut free dessert without apples\"? Easy as pie. Or create your own recipe manager without making your users create tons of tags to find what they're looking for next time. It's all possible.\n" +
//                        "Have your own recipe database? Great! Let us help you get it under control. With our API, your users will be able to find the recipes they want.",
//                30,
//                "https://spoonacular.com/recipeImages/634028-556x370.jpg"
//            )
//        RecipeDetailContent(
//            recipe = dummyRecipes
//        )
//    }
//}