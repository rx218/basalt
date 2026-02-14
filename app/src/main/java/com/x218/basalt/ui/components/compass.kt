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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
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
            .rotate(north)
            .graphicsLayer(rotationZ = northRotation)
    ) {
        Image(
            painter = painterResource(id = R.drawable.rose),
            modifier = Modifier
                .align(Alignment.Center),
            contentDescription = "Compass Rose"
        )
        Image(
            painter = painterResource(id = R.drawable.needle),
            modifier = Modifier
                .rotate(kaabaBearing)
                .graphicsLayer(rotationZ = needleRotation)
                .scale(0.75f)
                .align(Alignment.Center),
            contentDescription = "Needle Kaaba"
        )
    }
}

@Preview
@Composable
fun PreviewCompassAdjustable() {
    Column {
        var v1 by remember { mutableFloatStateOf(0f) }
        var v2 by remember { mutableFloatStateOf(0f) }
        Compass(v1, v2)
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