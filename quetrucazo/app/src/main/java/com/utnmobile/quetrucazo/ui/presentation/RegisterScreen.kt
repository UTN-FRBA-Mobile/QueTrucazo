package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.R
import com.utnmobile.quetrucazo.ui.viewmodel.auth.AuthViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(navigateTo: NavigateTo) {
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
                label = { Text("Username") },
                singleLine = true,
                keyboardActions = KeyboardActions { keyboardController?.hide() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                keyboardActions = KeyboardActions { keyboardController?.hide() },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        isLoading = true
                        authViewModel.register(username, password, {
                            isLoading = false
                            navigateTo(Screen.Main, emptyMap())
                        }, {
                            isLoading = false
                            errorMessage = it
                        })
                    },
                    colors = colorBoton()
                ) {
                    Text("Registrarme")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("¿Ya tienes cuenta?")

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "Iniciar sesión",
                modifier = Modifier.clickable {
                    navigateTo(Screen.Login, emptyMap())
                },
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                )
            )
        }
    }
}