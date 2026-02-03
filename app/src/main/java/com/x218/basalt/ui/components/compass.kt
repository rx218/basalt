package com.x218.basalt.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.x218.basalt.CompassState
import com.x218.basalt.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

@Composable
fun Compass(compassState: CompassState) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.rose),
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(compassState.north),
            contentDescription = "Compass Rose"
        )
        Image(
            painter = painterResource(id = R.drawable.needle),
            modifier = Modifier
                .rotate(compassState.kaabaBearing)
                .scale(0.75f)
                .align(Alignment.Center),
            contentDescription = "Needle Kaaba"
        )
    }
}

@Preview
@Composable
fun PreviewCompass() {
    val cs = CompassState(0f, 90f)
    Compass(cs)
    for(i in 0..360) {
        cs.kaabaBearing += i
        cs.north -= i/2
    }
    runBlocking { delay(1.seconds) }
}

@Preview
@Composable
fun PreviewCompassAdjustable() {
    Column {
        var v1 by remember { mutableFloatStateOf(0f) }
        var v2 by remember { mutableFloatStateOf(0f) }
        Compass(CompassState(v1, v2))
        Slider(
            value = v1,
            onValueChange = { v1 = it },
            valueRange = 0f..360f,
            steps = 10
        )
        Text(text = v1.toString())
        Slider(
            value = v2,
            onValueChange = { v2 = it },
            valueRange = 0f..360f,
            steps = 10
        )
        Text(text = v2.toString())
    }
}