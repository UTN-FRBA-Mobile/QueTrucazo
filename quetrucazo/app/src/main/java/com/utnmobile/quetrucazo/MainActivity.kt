package com.utnmobile.quetrucazo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.utnmobile.quetrucazo.ui.presentation.LoginScreen
import com.utnmobile.quetrucazo.ui.presentation.RegisterScreen
import com.utnmobile.quetrucazo.ui.theme.QueTrucazoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QueTrucazoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showLogin by remember { mutableStateOf(true) }

                    if (showLogin) {
                        LoginScreen(
                            onLogin = { username, password ->
                                // Handle login logic here
                                println("Login with username: $username, password: $password")
                            },
                            onNavigateToRegister = { showLogin = false }
                        )
                    } else {
                        RegisterScreen(
                            onRegister = { username, password ->
                                // Handle registration logic here
                                println("Register with username: $username, password: $password")
                            },
                            onNavigateToLogin = { showLogin = true }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}