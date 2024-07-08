package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.utnmobile.quetrucazo.services.SocketIOManager

@Composable
fun PlayAgainDialog(
    onDismissRequest: () -> Unit,
    gameId: Int,
    userId: Int
) {
    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth(),
            color = dialogColor()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Esperando rival...",
                    style = MaterialTheme.typography.titleLarge,
                    color = dialogTextColor()
                )

                Spacer(modifier = Modifier.height(16.dp))

                CircularProgressIndicator(color = dialogTextColor())

                Spacer(modifier = Modifier.height(64.dp))

                BotonImagen2(
                    onClick = {
                        onDismissRequest()
                        SocketIOManager.noPlayAgain(userId, gameId)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    text = "Cancelar"
                )

            }
        }
    }

}