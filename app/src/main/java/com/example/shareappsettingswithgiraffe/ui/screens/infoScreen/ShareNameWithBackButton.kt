package com.example.shareappsettingswithgiraffe.ui.screens.infoScreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size


import androidx.compose.material.IconButton


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder


import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.unit.dp


@Composable
fun ShareNameWithBackButton(
    secId: String,
    isFavorite: Boolean,
    shareName: String = "ShareName",
    navigateUp: () -> Unit = {},
    onHeartClick: (secId: String, isLiked: Boolean) -> Unit,
) {

    Surface(
        Modifier
            .height(70.dp)
            .fillMaxWidth(),
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(onClick = navigateUp) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "NoDescription"
                )
            }


            Text(
                text = shareName,
                style = MaterialTheme.typography.displayMedium,
            )


            val icon: ImageVector
            if (isFavorite) {
                icon = Icons.Filled.Favorite
            } else {
                icon = Icons.Filled.FavoriteBorder
            }

            IconButton(
                onClick = { onHeartClick(secId, !isFavorite) }

            ) {
                androidx.compose.material3.Icon(
                    imageVector = icon,
                    contentDescription = "NoDescription",
                    modifier = Modifier
                        .size(18.dp),
                )
            }

        }

    }

}