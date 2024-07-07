package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.R
import com.utnmobile.quetrucazo.ui.viewmodel.auth.AuthViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navigateTo: NavigateTo) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val authViewModel = viewModel<AuthViewModel>()

    errorMessage?.let {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text("Error de Login") },
            text = { Text(it) },
            confirmButton = {
                Button(
                    onClick = { errorMessage = null },
                    colors = colorBoton()
                ) {
                    Text("OK")
                }
            },
            containerColor = dialogColor()
        )
    }

    BackgroundBox(imageRes = R.drawable.login_background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                singleLine = true,
                keyboardActions = KeyboardActions { keyboardController?.hide() },
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledTextColor = Color.White.copy(alpha = 0.6f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                    focusedContainerColor = Color.Black.copy(alpha = 0.6f),
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.6f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                keyboardActions = KeyboardActions { keyboardController?.hide() },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledTextColor = Color.White.copy(alpha = 0.6f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                    focusedContainerColor = Color.Black.copy(alpha = 0.6f),
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.6f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Button(
                    onClick = {
                        isLoading = true
                        authViewModel.login(username, password, {
                            isLoading = false
                            navigateTo(Screen.Main, emptyMap())
                        }, {
                            isLoading = false
                            errorMessage = it
                        })
                    },
                    colors = colorBoton()
                ) {
                    Text("INICIAR SESIÓN")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = Color.Black.copy(alpha = 0.6f), // Color de fondo semi-transparente
                shape = MaterialTheme.shapes.small, // Forma del fondo
                modifier = Modifier.padding(horizontal = 8.dp) // Espaciado alrededor del texto
            ) {
                Text(
                    "¿No tienes cuenta?",
                    color = Color.White,
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    navigateTo(Screen.Register, emptyMap())
                },
                modifier = Modifier
                    .width(180.dp)
                    .height(30.dp),
                colors = colorBotonDisable()
            ) {
                Text("REGISTRARSE", fontSize = 11.sp)
            }

        }
    }

}


