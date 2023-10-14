package com.example.shareappsettingswithgiraffe.ui.screens.infoScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shareappsettingswithgiraffe.R

@Composable
fun InfoScreenSharesInfo(
    shareInfoItem: DetailedShareInfo
) {

    var fullName = "Нет информации"
    if (shareInfoItem.fullName.isNotBlank()) {
        fullName = shareInfoItem.fullName
    }

    var prevPrice = "Нет информации"
    if (shareInfoItem.prevPrice != 0.0) {
        prevPrice = shareInfoItem.prevPrice.toString()
    }

    var openPrice = "Нет информации"
    if (shareInfoItem.openPrice != null) {
        openPrice = shareInfoItem.openPrice.toString()
    }

    var lastPrice = "Нет информации"
    if (shareInfoItem.lastPrice != null) {
        lastPrice = shareInfoItem.lastPrice.toString()
    }

    var lastDealTime = "Нет информации"
    if (shareInfoItem.lastDealTime != "") {
        lastDealTime = shareInfoItem.lastDealTime.toString()
    }


    val paddingBetweenTextColumns = 10.dp

    Column(
        modifier = Modifier
            .padding(top = 15.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {


        val textOpasityModifier = Modifier.alpha(0.5f)
        Text(
            text = "Полное название",
            style = MaterialTheme.typography.displayMedium,
            modifier = textOpasityModifier

        )


        Text(
            text = fullName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = paddingBetweenTextColumns)

        )

        Text(
            text = "Цена закрытия прошлых торгов, Р.",
            style = MaterialTheme.typography.displayMedium,
            modifier = textOpasityModifier
        )

        Text(
            text = prevPrice,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = paddingBetweenTextColumns)
        )

        Text(
            text = "Цена открытия, Р.",
            style = MaterialTheme.typography.displayMedium,
            modifier = textOpasityModifier
        )

        Text(
            text = openPrice,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = paddingBetweenTextColumns)
        )

        Text(
            text = "Цена последней сделки, Р.",
            style = MaterialTheme.typography.displayMedium,
            modifier = textOpasityModifier
        )

        Text(
            text = lastPrice,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = paddingBetweenTextColumns)
        )

        Text(
            text = "Время последней сделки",
            style = MaterialTheme.typography.displayMedium,
            modifier = textOpasityModifier
        )
        Text(
            text = lastDealTime,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = paddingBetweenTextColumns)
        )

    }

}