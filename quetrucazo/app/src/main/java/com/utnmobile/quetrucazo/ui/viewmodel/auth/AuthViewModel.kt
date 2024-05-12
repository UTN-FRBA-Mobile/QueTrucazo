package com.utnmobile.quetrucazo.ui.viewmodel.auth

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import android.content.Context
import com.utnmobile.quetrucazo.services.ApiService
import com.utnmobile.quetrucazo.services.AuthResponse
import com.utnmobile.quetrucazo.services.Credentials
import com.utnmobile.quetrucazo.services.User
import com.utnmobile.quetrucazo.services.parseError
import com.utnmobile.quetrucazo.services.provideApiService
import com.utnmobile.quetrucazo.services.provideRetrofit

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val apiService: ApiService = provideApiService(provideRetrofit())

    private var _user: User? = null

    val user: User?
        get() = _user

    fun login(username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val response = try {
                apiService.loginUser(Credentials(username, password))
            } catch (e: Exception) {
                onError("Hubo un error al iniciar sesión 1")
                null
            }

            if (response != null) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse == null) {
                        onError("Hubo un error al iniciar sesión 2")
                    } else {
                        saveAuth(authResponse)
                        onSuccess()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = parseError(errorBody)
                    when (errorResponse?.type) {
                        "FORM_INVALID_FIELD" -> onError("Usuario o contraseña incorrecta")
                        else -> onError("Error desconocido")
                    }
                }
            }
        }
    }

    private fun saveAuth(res: AuthResponse) {
        _user = res.user
        val sharedPreferences = context.getSharedPreferences("QueTrucazoPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", res.token.token).apply()
    }
}