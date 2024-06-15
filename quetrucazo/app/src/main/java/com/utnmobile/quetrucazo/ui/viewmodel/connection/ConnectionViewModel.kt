package com.utnmobile.quetrucazo.ui.viewmodel.connection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utnmobile.quetrucazo.services.SocketIOManager
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

    fun connect(userId: Int, onConnect: () -> Unit) {
        SocketIOManager.connect(userId, onConnect) {
            updateShowDisconnect(true)
        }
    }
}