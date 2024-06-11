package com.utnmobile.quetrucazo.ui.viewmodel.connection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConnectionViewModel : ViewModel() {
    private val _showDisconnect = MutableStateFlow(false)

    val showDisconnect: StateFlow<Boolean> = _showDisconnect

    fun updateShowDisconnect(value: Boolean) {
        viewModelScope.launch {
            _showDisconnect.emit(value)
        }
    }
}