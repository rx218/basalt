package com.x218.basalt.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.x218.basalt.data.PermissionState
import com.x218.basalt.ui.theme.AppTheme

@Composable
fun PermissionDialog(perms: PermissionState, onDismiss: () -> Unit) {
    val colorFine = if (perms.fine) { Color.Green } else { Color.Red }
    val colorCoarse = if (perms.coarse) { Color.Green } else { Color.Red }
    val coarseGrantedTxt = if(perms.coarse) { "[Granted]" } else { "[Not Granted]" }
    val fineGrantedTxt = if(perms.fine) { "[Granted]" } else { "[Not Granted]" }

    AlertDialog(
        onDismissRequest = { onDismiss() },
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
                    Text(
                        "Used to determine your general area (city or region). This allows the app to calculate the Qibla direction without requiring exact positioning.",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text("")

                    Text("Precise Location $fineGrantedTxt", color = colorFine)
                    Text(
                        "Used to provide a more accurate Qibla direction based on your exact position. This is especially helpful when you are traveling or in large cities.",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text("")

                    Text("Go to app settings to grant the permissions")
                }
            },
        confirmButton = {
            ElevatedButton(
                onClick = { onDismiss() }
            ) {
                Text("Okay")
            }
        }
    )
}

@Preview(widthDp = 500)
@Composable
fun PreviewPermissionDialog() {
    AppTheme() {
        PermissionDialog(PermissionState(coarse = true, fine = false), {})
    }
}