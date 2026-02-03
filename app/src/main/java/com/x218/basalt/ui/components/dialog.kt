package com.x218.basalt.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlin.system.exitProcess

@Composable
@Preview
fun PermissionDialog() {
    AlertDialog(onDismissRequest = { exitProcess(0) },
        title = {
            Text("Grant Location Permission")
        }, text = {
            Text(
                "Qibla finder" +
                        " app requires location permission to work. " +
                        "Please grant the required permissions"
            )
        }, confirmButton = {
            TextButton(onClick = {
                // launch permission utility
            }) {
                Text("Okay")
            }
        }
    )
}
