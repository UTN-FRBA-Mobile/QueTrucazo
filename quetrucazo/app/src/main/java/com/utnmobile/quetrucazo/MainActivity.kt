package com.utnmobile.quetrucazo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.utnmobile.quetrucazo.ui.presentation.AppNavigation
import com.utnmobile.quetrucazo.ui.theme.QueTrucazoTheme
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.ui.viewmodel.connection.ConnectionViewModel
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QueTrucazoTheme {
                // Use ViewModel with factory
                viewModel<MusicViewModel>(factory = MusicViewModelFactory(applicationContext))
                viewModel<ConnectionViewModel>()
                AppNavigation(applicationContext)
            }
        }
    }
}