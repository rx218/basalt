package com.x218.basalt.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.x218.basalt.data.PermissionState
import kotlin.system.exitProcess

@Composable
fun PermissionDialog(perms: PermissionState) {
    val colorFine = if (perms.fine) { Color.Green } else { Color.Red }
    val colorCoarse = if (perms.coarse) { Color.Green } else { Color.Red }
    val coarseGrantedTxt = if(perms.coarse) { "[Granted]" } else { "[Not Granted]" }
    val fineGrantedTxt = if(perms.fine) { "[Granted]" } else { "[Not Granted]" }

    AlertDialog(onDismissRequest = { exitProcess(0) },
        title = {
            Text("Grant Location Permission")
        },
        text =
            {
                Column() {
                    Text("Qibla finder requires location permissions to work.")
                    Text("Please grant the following permissions:")

                    Text("")

                    Text("Approximate Location $coarseGrantedTxt", color = colorCoarse)
                    Text("Used to determine your general area (city or region). This allows the app to calculate the Qibla direction without requiring exact positioning.")

                    Text("")

                    Text("Precise Location $fineGrantedTxt", color = colorFine)
                    Text("Used to provide a more accurate Qibla direction based on your exact position. This is especially helpful when you are traveling or in large cities.")

                    Text("")

                    Text("Go to app settings to grant the permissions")
                }
            },
        confirmButton = {
            Text("Okay")
        }
    )
}

@Preview(widthDp = 500)
@Composable
fun PreviewPermissionDialog() {
    PermissionDialog(PermissionState(coarse = true, fine = false))
}