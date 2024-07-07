package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.utnmobile.quetrucazo.services.SocketIOManager

@Composable
fun TrucoDialog(
    onDismissRequest: () -> Unit,
    gameId: Int,
    userId: Int,
    call: String,
    onMyDialogText: (String) -> Unit,
    canCallEnvido: Boolean,
) {
    var showEnvidoOptions by remember { mutableStateOf(false) }
    val buttonColors = colorBoton()
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth(),
            color = dialogColor()
        ) {
            Column(modifier = Modifier.padding(16.dp)){
                Row( modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically){
                    Text(text = "¡${call.replace("_", " ")}!",
                        style = TextStyle(
                            fontSize = 30.sp,
                            color = dialogTextColor(),
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.animateContentSize()
                    )


                }
                if (showEnvidoOptions) {
                    Row (modifier = Modifier
                        .fillMaxWidth()

                    ) {
                        Button(onClick = {
                            onMyDialogText("Envido")
                            SocketIOManager.envidoGoFirst(userId, gameId, "ENVIDO")
                        },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = buttonColors) {
                            Text(text = "ENVIDO")
                        }

                    }
                    Row (modifier = Modifier
                        .fillMaxWidth()

                    ) {
                        Button(onClick = {
                            onMyDialogText("Real envido")
                            SocketIOManager.envidoGoFirst(userId, gameId, "REAL_ENVIDO")
                        },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = buttonColors) {
                            Text(text = "REAL ENVIDO")
                        }

                    }
                    Row (modifier = Modifier
                        .fillMaxWidth()

                    ) {
                        Button(onClick = {
                            onMyDialogText("Falta envido")
                            SocketIOManager.envidoGoFirst(userId, gameId, "FALTA_ENVIDO")
                        },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = buttonColors) {
                            Text(text = "FALTA ENVIDO")
                        }

                    }
                    Row (modifier = Modifier
                        .fillMaxWidth()

                    ) {
                        Button(onClick = {
                            showEnvidoOptions = false
                        },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = buttonColors) {
                            Text(text = "ATRÁS")
                        }

                    }
                } else {
                    if (canCallEnvido) {
                        Row (modifier = Modifier
                            .fillMaxWidth()

                        ) {
                            Button(onClick = {
                                showEnvidoOptions = true
                            },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = buttonColors) {
                                Text(text = "ENVIDO")
                            }

                        }
                    }

                    Row (modifier = Modifier
                        .fillMaxWidth()

                    ) {
                        Button(onClick = {
                            onDismissRequest()
                            onMyDialogText("Quiero")
                            SocketIOManager.trucoAccept(userId,gameId,true)
                        },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = buttonColors) {
                            Text(text = "¡QUIERO!")
                        }

                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()

                    ) {
                        val nextCall: String = if (call == "TRUCO"){
                            "RETRUCO"
                        } else "VALE_CUATRO"
                        Button(
                            onClick = {
                                onDismissRequest()
                                val serverCall = when (nextCall) {
                                    "RETRUCO" -> "Quiero retruco"
                                    "VALE_CUATRO" -> "Quiero vale cuatro"
                                    else -> "Truco"
                                }
                                onMyDialogText(serverCall)
                                SocketIOManager.trucoCall(userId, gameId,nextCall)

                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = buttonColors,
                            enabled = (call != "VALE_CUATRO")
                        ) {
                            Text("¡${nextCall.replace("_", " ")}!")
                        }

                    }
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.25f),
                    ) {
                        Button(onClick = {
                            onDismissRequest()
                            onMyDialogText("No quiero")
                            SocketIOManager.trucoAccept(userId,gameId,false)             },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = buttonColors) {
                            Text(text = "MAZO")
                        }

                    }
                }
            }
        }
    }

}