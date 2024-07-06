package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.services.SocketIOManager
import com.utnmobile.quetrucazo.ui.presentation.colorBoton
import com.utnmobile.quetrucazo.ui.presentation.colorBotonDisable

@Composable
fun PlayGameScreen(
    modifier: Modifier = Modifier,
    userId: Int,
    gameId: Int,
    myTurn: Boolean,
    onMyDialogText: (String) -> Unit,
    showEnvidoAnswerOptions: Boolean,
    trucoCall: String?,
    trucoCaller: UserId?,
    isFirstStep: Boolean,
    wasEnvidoCalled: Boolean,
    envidoCalls: List<String>,
) {
    var showEnvidoCallOptions by remember { mutableStateOf(false) }

    val disableFaltaEnvido = envidoCalls.lastOrNull() == "FALTA_ENVIDO"
    val disableRealEnvido = envidoCalls.lastOrNull() == "REAL_ENVIDO" || disableFaltaEnvido
    val disableEnvido = envidoCalls.size > 1 && envidoCalls.takeLast(2).all { it == "ENVIDO" } || disableRealEnvido

    if (myTurn) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            if (showEnvidoCallOptions || showEnvidoAnswerOptions) {
                val buttonHeight = 64.dp

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(
                        onClick = {
                            onMyDialogText("Envido")
                            SocketIOManager.envido(userId, gameId, "ENVIDO")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight),
                        shape = RectangleShape,
                        colors = if (disableEnvido) colorBotonDisable() else colorBoton(),
                        enabled = !(disableEnvido)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text("ENVIDO", fontSize = 16.sp, maxLines = 1)
                        }
                    }

                    Button(
                        onClick = {
                            onMyDialogText("Real envido")
                            SocketIOManager.envido(userId, gameId, "REAL_ENVIDO")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight),
                        shape = RectangleShape,
                        colors = if (disableRealEnvido) colorBotonDisable() else colorBoton(),
                        enabled = !(disableRealEnvido)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text("REAL ENVIDO", fontSize = 16.sp, maxLines = 2)
                        }
                    }

                    Button(
                        onClick = {
                            onMyDialogText("Falta envido")
                            SocketIOManager.envido(userId, gameId, "FALTA_ENVIDO")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight),
                        shape = RectangleShape,
                        colors = if (disableFaltaEnvido) colorBotonDisable() else colorBoton(),
                        enabled = !(disableFaltaEnvido)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text("FALTA ENVIDO", fontSize = 16.sp, maxLines = 2)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showEnvidoAnswerOptions) {
                    val buttonHeight = 64.dp

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val lightButtonColors =
                            colorBotonDisable()

                        Button(
                            onClick = {
                                onMyDialogText("Quiero")
                                SocketIOManager.answerEnvido(userId, gameId, true)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(buttonHeight),
                            shape = RectangleShape,
                            colors = lightButtonColors
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text("QUIERO", fontSize = 16.sp, maxLines = 1)
                            }
                        }

                        Button(
                            onClick = {
                                onMyDialogText("No quiero")
                                SocketIOManager.answerEnvido(userId, gameId, false)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(buttonHeight),
                            shape = RectangleShape,
                            colors = lightButtonColors
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text("NO QUIERO", fontSize = 16.sp, maxLines = 2)
                            }
                        }
                    }
                } else {
                    val buttonColors =
                        colorBoton()
                    val grayButtonColors =
                        colorBotonDisable()
                    val buttonHeight = 48.dp

                    val nextCall =
                        if (trucoCall == null || trucoCall == "") "TRUCO" else if (trucoCall == "TRUCO") "RETRUCO" else if (trucoCall == "RETRUCO") "VALE_CUATRO" else "TRUCO"

                    Button(
                        onClick = {
                            SocketIOManager.trucoCall(userId, gameId, nextCall)
                            when (nextCall) {
                                "RETRUCO" -> onMyDialogText("Quiero retruco")
                                "VALE_CUATRO" -> onMyDialogText("Quiero vale cuatro")
                                else -> onMyDialogText("Truco")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight),
                        shape = RectangleShape,
                        colors = if (showEnvidoCallOptions) grayButtonColors else buttonColors,
                        enabled = !showEnvidoCallOptions && (trucoCall != "VALE_CUATRO") && (trucoCaller != userId)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(nextCall.replace("_", " ").replace("VALE CUATRO", "V CUATRO"), fontSize = 16.sp, maxLines = 1)
                        }
                    }

                    Button(
                        onClick = {
                            if (isFirstStep && !wasEnvidoCalled) {
                                showEnvidoCallOptions = !showEnvidoCallOptions
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight),
                        shape = RectangleShape,
                        colors = if (isFirstStep && !wasEnvidoCalled) buttonColors else grayButtonColors,
                        enabled = isFirstStep && !wasEnvidoCalled
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("ENVIDO", fontSize = 16.sp, maxLines = 1)
                        }
                    }

                    Button(
                        onClick = {
                            SocketIOManager.goToDeck(userId, gameId)
                            onMyDialogText("Me voy al mazo")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight),
                        shape = RectangleShape,
                        colors = buttonColors
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("MAZO", fontSize = 16.sp, maxLines = 1)
                        }
                    }
                }
            }
        }
    } else {
        showEnvidoCallOptions = false
    }
}