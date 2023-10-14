package com.example.shareappsettingswithgiraffe.ui.components

import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray

import androidx.compose.ui.unit.dp

@Composable
fun AppDivider (modifier: Modifier = Modifier){

    Divider(
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
        thickness = 1.dp, modifier = modifier
    )

}