package com.devspace.myapplication.common.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.google.android.material.textview.MaterialTextView

@Composable
fun HtmlTextUI(
    text: String,
    maxLine: Int? = null,
    fontSize: Int = 15
) {
    val spannedText = HtmlCompat.fromHtml(text, 0)
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()

    AndroidView(modifier = Modifier,
        factory = { it ->
            MaterialTextView(it).apply {
                setTextColor(textColor)
                maxLine?.let {
                    maxLines = it
                }
                textSize = fontSize.toFloat()
            }
        },
        update = {it.text = spannedText}
    )
}