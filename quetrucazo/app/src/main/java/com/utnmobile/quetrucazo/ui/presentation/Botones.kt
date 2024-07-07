package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.utnmobile.quetrucazo.R

@Composable
fun BotonImagen(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
    fontSize: TextUnit = TextUnit.Unspecified,
    imagen: Int,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape

) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = colorBoton(),
        contentPadding = PaddingValues(0.dp),
        enabled = enabled,
        shape = shape
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(imagen),
                contentDescription = null, // decorative
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(text, fontSize = fontSize)
        }
    }
}


@Composable
fun BotonImagen1(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
    fontSize: TextUnit = TextUnit.Unspecified,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape

) {
    BotonImagen(
        onClick = onClick,
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        imagen = R.drawable.textura_boton_madera1,
        enabled = enabled,
        shape = shape
    )
}

@Composable
fun BotonImagen2(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
    fontSize: TextUnit = TextUnit.Unspecified,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape

) {
    BotonImagen(
        onClick = onClick,
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        imagen = R.drawable.textura_boton_madera2,
        enabled = enabled,
        shape = shape
    )
}


