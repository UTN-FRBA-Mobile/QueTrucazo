package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(onRegister: (String, String) -> Unit, onNavigateTo: OnNavigateTo) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
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
            keyboardActions = KeyboardActions { keyboardController?.hide() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onRegister(username, password) }) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("¿Ya tienes cuenta?")

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            "Iniciar sesión",
            modifier = Modifier.clickable {
                onNavigateTo(Screen.Login)
            },
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary,
            )
        )
    }
}