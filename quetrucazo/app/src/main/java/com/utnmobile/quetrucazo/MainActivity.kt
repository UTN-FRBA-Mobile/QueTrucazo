package com.utnmobile.quetrucazo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.utnmobile.quetrucazo.ui.presentation.AppNavigation
import com.utnmobile.quetrucazo.ui.theme.QueTrucazoTheme
import com.utnmobile.quetrucazo.music.MusicModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.music.MusicModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QueTrucazoTheme {
                // Use ViewModel with factory
                val musicModel: MusicModel = viewModel(factory = MusicModelFactory(applicationContext))
                AppNavigation(musicModel)
            }
        }
    }
}