package com.example.shareappsettingswithgiraffe.ui.screens.newsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.SpanStyle

import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shareappsettingswithgiraffe.MainViewModel

import com.example.shareappsettingswithgiraffe.data.database.ShareEntity
import com.example.shareappsettingswithgiraffe.data.database.ShareNews
import com.example.shareappsettingswithgiraffe.ui.components.AppDivider
import com.example.shareappsettingswithgiraffe.ui.theme.bright_brown_color

import com.example.shareappsettingswithgiraffe.utils.StrategyOptions
import kotlin.math.roundToInt

@Composable
fun NewsScreen(
    NewsScreenViewModel: NewsScreenViewModel,
    onShareClick: (String) -> Unit = {},
    scaffoldBottomPadding: Dp,
    mainViewModel: MainViewModel,

    ) {

//    val isDataUploading = mainViewModel.downLoadingState.collectAsState()
    val listOfShareNews1 = NewsScreenViewModel.listOfNews1.collectAsState(initial = emptyList())
    val listOfAllShares =
        NewsScreenViewModel.listOfAllShares.collectAsState(initial = emptyList()).value


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, bottom = scaffoldBottomPadding),

        ) {

        Text(
            "Cписок актуальных рекомендаций к покупке акций:",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 10.dp, bottom = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )


        if (listOfShareNews1.value.isNotEmpty() && listOfAllShares.isNotEmpty()) {
            ShowResultsForNews(
                listOfShareNews = listOfShareNews1,
                listOfAllShares = listOfAllShares,
                onShareClick = onShareClick,
            )
        } else {
            NoNews()
        }
    }

}

@Composable
fun ShowResultsForNews(
    listOfAllShares: List<ShareEntity>,
    onDeleteNewsItemClick: () -> Unit = {},
    onShareClick: (String) -> Unit = {},
    listOfShareNews: State<List<ShareNews>>,
) {

    val listOfShareNews1 = listOfShareNews.value
    var comparisonTargetText = ""
    when (listOfShareNews1[0].comparisonTarget) {
        StrategyOptions.СomparisonTarget.MAX_PRICE -> {
            comparisonTargetText = "максимальной ценой"
        }

        StrategyOptions.СomparisonTarget.MIN_PRICE -> {
            comparisonTargetText = "минимальной ценой"
        }

        StrategyOptions.СomparisonTarget.START_OF_THE_PERIOD -> {
            comparisonTargetText = "ценой начала периода"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        listOfShareNews1.forEachIndexed { index: Int, it: ShareNews ->
            val listOfSecId = listOfShareNews1.map { it.secId }
            println("NewsScreenComposable: listOfSecId = $listOfSecId")

            val secId = it.secId

            val name = listOfAllShares.find {
                it.secId == secId
            }!!.shortName

            BaseShareRow(
                number = index + 1,
                name = name,
                comparisonTargetText = comparisonTargetText,
                newsItem = it,
                onDeleteClick = onDeleteNewsItemClick,
                onShareClick = { onShareClick(it.secId) },
                modifier = Modifier.heightIn(min = 70.dp)
            )
            AppDivider()
        }
    }


}

@Composable
fun NoNews() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Согласно выбранной стратегии подходящих акций нет",
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun BaseShareRow(
    number: Int,
    name: String,
    comparisonTargetText: String,
    modifier: Modifier = Modifier,
    newsItem: ShareNews,
    onDeleteClick: () -> Unit = {},
    onShareClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onShareClick() }
            .wrapContentHeight(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val changePercent = (newsItem.changePercent.toDouble() * 100.0).roundToInt() / 100.0
        val annotatedString = buildAnnotatedString {
            val text =
                "Акция ${name} изменилась на ${changePercent}% за ${newsItem.changePeriod} в сравнении c $comparisonTargetText"
            val start = text.indexOf(name)
            val end = start + name.length
            append(text)
            addStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.None,
                    fontWeight = FontWeight.Bold
                ),
                start,
                end
            )
            addStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.None,
                ),
                start = text.indexOf("$changePercent%"),
                end = text.indexOf("$changePercent%") + "$changePercent%".length
            )

        }

        Text(
            text = "$number",
            style = MaterialTheme.typography.displayMedium,
            color = bright_brown_color,
            fontSize = 35.sp,
            modifier = Modifier.padding(end = 10.dp, top = 0.dp),
            textAlign = TextAlign.Start
        )

        Column(modifier = Modifier
            .weight(1f)
            .padding(top = 6.dp)) {
            Text(
                text = annotatedString, modifier = Modifier,
                lineHeight = 20.sp
            )

            RowWithPrices(
                modifier = Modifier.padding(bottom = 14.dp, top = 10.dp),
                newsItem = newsItem
            )
        }

        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp)
        )
    }
}

@Composable
private fun RowWithPrices(
    modifier: Modifier,
    newsItem: ShareNews,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text("Текущая цена: ${newsItem.currentPrice}р. ")
    }
}
