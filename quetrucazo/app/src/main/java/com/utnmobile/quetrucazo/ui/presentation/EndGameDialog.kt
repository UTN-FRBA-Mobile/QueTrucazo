package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.utnmobile.quetrucazo.services.SocketIOManager
import com.utnmobile.quetrucazo.ui.presentation.game.PointCounter

@Composable
fun EndGameDialog(
    onDismissRequest: () -> Unit,
    myPoints: Int,
    opponentPoints: Int,
    isWinner: Boolean,
    gameId: Int,
    userId: Int,

) {

    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth(),
            color = dialogColor()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.25f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        if (isWinner) "¡Felicitaciones, has ganado el juego!" else "¡Has perdido el juego!",
                        style = TextStyle(
                            fontSize = 30.sp,
                            color = dialogTextColor(),
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.animateContentSize()
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.25f),

                    ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        PointCounter(text = "TU", points = myPoints)
                        PointCounter(text = "ÉL", points = opponentPoints)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.25f)
                ) {

                    BotonImagen2(
                        onClick = {
                            onDismissRequest()
                            SocketIOManager.playAgain(userId, gameId)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        text = "VOLVER A JUGAR"
                    )

                                  }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    BotonImagen2(
                        onClick = {
                            onDismissRequest()
                            SocketIOManager.noPlayAgain(userId, gameId)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        text = "IR AL MENU"
                    )


                }

            }
        }
    }


}

@Preview
@Composable
fun YouWinDialogPreview() {
    EndGameDialog(
        onDismissRequest = { },
        myPoints = 12,
        opponentPoints = 23,
        isWinner = false,
        gameId = 1,
        userId = 2
    )
}
