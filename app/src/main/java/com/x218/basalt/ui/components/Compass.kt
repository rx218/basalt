package com.x218.basalt.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.x218.basalt.R

@Composable
fun Compass(north: Float, kaabaBearing: Float) {
    val northRotation by animateFloatAsState(
        targetValue = north,
        animationSpec = spring(stiffness = Spring.StiffnessLow), // Low stiffness = smoother
        label = "Rose Rotation"
    )

    val needleRotation by animateFloatAsState(
        targetValue = kaabaBearing,
        animationSpec = spring(stiffness = Spring.StiffnessLow), // Low stiffness = smoother
        label = "Needle Rotation"
    )

    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .graphicsLayer(rotationZ = northRotation)
    ) {
        Text("N", modifier = Modifier.align(Alignment.TopCenter), color = Color.Red, fontWeight = FontWeight.ExtraBold)
        Text("S", modifier = Modifier.align(Alignment.BottomCenter).rotate(180f), fontWeight = FontWeight.Bold)
        Text("E", modifier = Modifier.align(Alignment.CenterEnd).rotate(90f), fontWeight = FontWeight.Bold)
        Text("W", modifier = Modifier.align(Alignment.CenterStart).rotate(270f), fontWeight = FontWeight.Bold)
        Image(
            painter = painterResource(id = R.drawable.rose),
            modifier = Modifier
                .align(Alignment.Center),
            contentDescription = "Compass Rose"
        )
        Image(
            painter = painterResource(id = R.drawable.needle),
            modifier = Modifier
                .align(Alignment.Center)
                .scale(1.8f)
                .graphicsLayer(rotationZ = needleRotation),
            contentDescription = "Needle Kaaba"
        )
    }
}

@Preview
@Composable
fun PreviewCompassAdjustable() {
    Column {
        var north by remember { mutableFloatStateOf(0f) }
        var kaabaBearing by remember { mutableFloatStateOf(0f) }
        Compass(north, kaabaBearing)
        Slider(
            value = north,
            onValueChange = { north = it },
            valueRange = -180f..180f
        )
        Text(text = north.toString())
        Slider(
            value = kaabaBearing,
            onValueChange = { kaabaBearing = it },
            valueRange = -180f..180f
        )
        Text(text = kaabaBearing.toString())
        val rot by animateFloatAsState(
            targetValue = kaabaBearing
        )
        Text(text = "Hi", modifier = Modifier.graphicsLayer(rotationZ = rot))
    }
}