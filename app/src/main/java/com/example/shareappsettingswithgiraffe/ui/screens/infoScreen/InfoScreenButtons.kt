package com.example.shareappsettingswithgiraffe.ui.screens.infoScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoScreenButtons(
    infoScreenViewModel: infoScreenViewModel
) {

    val ButtonTextStyle = MaterialTheme.typography.bodyLarge
    val ButtonTextSize = 13.sp

    val buttonShape = RoundedCornerShape(10.dp)
    val buttonMinWidth = 120.dp
    val SCREEN_PADDIN = 12.dp
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = {
                    infoScreenViewModel.onEvent(InfoScreenEvents.OnWeekButtonClick)
                },
                modifier = Modifier.widthIn(min = buttonMinWidth),
                shape = buttonShape
            ) {
                Text(
                    text = "1 неделя",
                    style = ButtonTextStyle,
                    fontSize = ButtonTextSize
                )
            }


            OutlinedButton(
                onClick = {
                    infoScreenViewModel.onEvent(InfoScreenEvents.OnMonthButtonClick)

                },
                modifier = Modifier.widthIn(min = buttonMinWidth),
                shape = buttonShape

            ) {
                Text(
                    text = "1 месяц",
                    style = ButtonTextStyle,
                    fontSize = ButtonTextSize
                )
            }
            OutlinedButton(
                onClick = {
                    infoScreenViewModel.onEvent(InfoScreenEvents.OnSixMonthButtonClick)
                },
                modifier = Modifier.widthIn(min = buttonMinWidth),
                shape = buttonShape
            ) {
                Text(
                    text = "6 месяцев",
                    style = ButtonTextStyle,
                    fontSize = ButtonTextSize
                )
            }
        }



        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = {
                    infoScreenViewModel.onEvent(InfoScreenEvents.OnYearButtonClick)
                },
                modifier = Modifier
                    .widthIn(min = buttonMinWidth),
                shape = buttonShape
            )
            {
                Text(
                    text = "1 год",
                    style = ButtonTextStyle,
                    fontSize = ButtonTextSize
                )
            }
            OutlinedButton(
                onClick = {
                    infoScreenViewModel.onEvent(InfoScreenEvents.OnFiveYearsButtonClick)
                },
                modifier = Modifier
                    .widthIn(min = buttonMinWidth),
                shape = buttonShape
            ) {
                Text(
                    text = "5 лет",
                    style = ButtonTextStyle,
                    fontSize = ButtonTextSize
                )
            }

            Spacer(
                modifier =
                Modifier.width(buttonMinWidth)

            )
        }

    }

}