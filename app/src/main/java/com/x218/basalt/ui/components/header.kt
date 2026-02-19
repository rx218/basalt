package com.x218.basalt.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.x218.basalt.R
import com.x218.basalt.ui.theme.AppTheme

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    onClickInfo: () -> Unit = {}
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
        actions = {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    TooltipAnchorPosition.Below),
                tooltip = {
                    PlainTooltip { Text("Info") }
                },
                state = rememberTooltipState()
            ) {
                IconButton(onClick = { onClickInfo() }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.info),
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
    AppTheme {
        Header()
    }
}