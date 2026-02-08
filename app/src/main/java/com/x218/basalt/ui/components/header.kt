package com.x218.basalt.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.x218.basalt.R

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    onClickInfo: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val padding = 4.dp
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.padding(padding),
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.rose),
                modifier = Modifier
                    .size(60.dp)
                    .padding(padding),
                contentDescription = stringResource(id = R.string.app_logo_description)
            )
        },
        actions = {
            TooltipBox(
                modifier = modifier,
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    TooltipAnchorPosition.Below),
                tooltip = {
                    PlainTooltip { Text("Info") }
                },
                state = rememberTooltipState()
            ) {
                IconButton(onClick = { /* Do something... */ }) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Add to favorites"
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewHeader() {
    Header()
}