package com.devspace.myapplication.detail.presentation.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.devspace.myapplication.common.ui.HtmlTextUI
import com.devspace.myapplication.R
import com.devspace.myapplication.common.RecipeDto
import com.devspace.myapplication.detail.presentation.RecipeDetailViewModel
import com.devspace.myapplication.ui.theme.poppinsFontFamily


@Composable
fun RecipeDetailScreen(recipeId: String, navHostController: NavHostController, recipeDetailViewModel: RecipeDetailViewModel) {

    val recipeDto by recipeDetailViewModel.uiRecipeDetail.collectAsState()
    recipeDetailViewModel.fetchRecipeDetail(recipeId)

    recipeDto?.let { recipe ->
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
                IconButton(onClick = {
                    navHostController.popBackStack()
                    recipeDetailViewModel.cleanRecipeId()
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
                model = recipe.image,
                contentDescription = "${recipe.title} Image"
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
                        text = recipe.title
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
                            text = recipe.readyInMinutes.toString() + " minutes"
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
                    RecipeDetailContent(recipe)
                }
            }
        }
    }
}

@Composable
fun RecipeDetailContent(recipe: RecipeDto) {
    HtmlTextUI(
        text = recipe.summary
    )
}