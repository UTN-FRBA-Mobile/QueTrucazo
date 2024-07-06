package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun BackgroundBox(imageRes: Int, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null, // decorative
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        content()
    }
}