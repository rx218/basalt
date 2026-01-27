package com.x218.basalt.ui

@Composable
fun PermissionDialog() {
    AlertDialog(onDismissRequest = { ExitApp },
    title = {
        Text("title")
    }, text = {
        Text("content")
    }, confirmButton = {
        TextButton(onClick = {
            // launch permission utility
        }) {
            Text("Okay")
        }
    })
}

fun GPSDialog() {}
