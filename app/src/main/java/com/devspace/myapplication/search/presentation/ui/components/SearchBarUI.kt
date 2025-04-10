package com.devspace.myapplication.search.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devspace.myapplication.ui.theme.poppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarUI(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    onSearchClicked: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .focusRequester(focusRequester),
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {
            keyboardController?.hide()
            focusRequester.freeFocus()
            focusManager.clearFocus()
            onSearchClicked.invoke()
        },
        active = false,
        onActiveChange = {},
        placeholder = {
            Text(
                fontSize = 16.sp,
                fontFamily = poppinsFontFamily,
                text = placeholder,
                fontWeight = FontWeight.Light,
                color = colorScheme.primary
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = colorScheme.primary
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Icon",
                tint = colorScheme.primary
            )
        }
    ) {

    }
}

@Preview
@Composable
private fun SearchBarUIPreview() {
    Column(
        modifier = Modifier
            .background(Color.DarkGray)
            .fillMaxSize()
            .padding(8.dp)
    ) {
        SearchBarUI(
            query = "",
            placeholder = "Search",
            onQueryChange = {},
            onSearchClicked = {},
        )
    }
}