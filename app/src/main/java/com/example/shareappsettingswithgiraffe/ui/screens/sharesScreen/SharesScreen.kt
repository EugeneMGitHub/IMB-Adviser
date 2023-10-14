package com.example.shareappsettingswithgiraffe.ui.screens.sharesScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState


import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.DataUpdateUIState
import com.example.StagesOfUpdate
import com.example.shareappsettingswithgiraffe.MainViewModel
import com.example.shareappsettingswithgiraffe.data.database.ShareEntity
import com.example.shareappsettingswithgiraffe.ui.components.AppDivider
import com.example.shareappsettingswithgiraffe.ui.screens.newsScreen.NewsScreenViewModel
import com.example.shareappsettingswithgiraffe.ui.theme.bright_brown_color
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharesScreen(
    ShareScreenViewModel: SharesScreenViewModel,
    onShareClick: (String) -> Unit,
    scaffoldBottomPadding: Dp,
    mainViewModel: MainViewModel,
    NewsScreenViewModel: NewsScreenViewModel,
    snackbarHostState: SnackbarHostState,
//    onFavoriteClick: () -> Unit,
) {


    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        ShareScreenViewModel.getTheLastUpdateDate()
    }


    val lastDateUpdate = ShareScreenViewModel.theLastUpdateDate
    val isLoading = mainViewModel.downLoadingState.collectAsState()
    val stageOfdownloading = mainViewModel.stageOfDownloading.collectAsState(initial = StagesOfUpdate(""))
    val allShares = ShareScreenViewModel.allShares.collectAsState(initial = emptyList())

    val loadingFlowState = mainViewModel.loadingFlow.collectAsState("Идет загрузка акций...")



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, bottom = scaffoldBottomPadding),
    ) {
        Text(
            "Cписок акций индекса МБ:",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 10.dp, bottom = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        when (isLoading.value) {
            DataUpdateUIState.IsLoading -> {
                NoShares(
                    mainText = loadingFlowState.value,
                    stageOfdownloading = stageOfdownloading.value.message
                )
            }
            DataUpdateUIState.LoadingHasCompleted -> {

                ShowSharesList(
                    allShares, ShareScreenViewModel, onShareClick,
                    textOfUpdate = "* Последняя дата обновления базы данных: $lastDateUpdate",
                    onTextClick = {

                        coroutineScope.launch{
                                snackbarHostState.showSnackbar(
                                message = "Будет обновление",
                                actionLabel = "Понятно",
                                duration = androidx.compose.material3.SnackbarDuration.Short
                            )
                            mainViewModel.setLoadingState()
                            mainViewModel.UpdateTheHistoryUltimate()
                            println("SavingHistoryInDB:   Updating is Complete")
                            mainViewModel.setCompletedState()
                            println("SavingHistoryInDB:   Have set  CompletetedState")
                            println("SavingHistoryInDB:   Now calculating the news")
                            // creates a list of news in the NewsScreen
                            NewsScreenViewModel.CalculateTheNews()
                            println("SavingHistoryInDB:   Stopped calculating the news")

                        }
                    }

                )


            }
        }


    }


}


@Composable
fun NoShares(
    mainText: String = "Идет загрузка акций...",
    stageOfdownloading : String = "",
) {
    Column(
        modifier = Modifier.fillMaxHeight().fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = mainText,
            textAlign = TextAlign.Start,
            modifier = Modifier.wrapContentWidth(),

        )
    }


}

@Composable
fun ShowSharesList(
    allShares: State<List<ShareEntity>>,
    ShareScreenViewModel: SharesScreenViewModel,
    onShareClick: (String) -> Unit,
    textOfUpdate: String = "",
    onTextClick: () -> Unit = {},

) {

    var textClickedTimes by remember {mutableStateOf(0)}

    LaunchedEffect(key1 = Unit,){
        if (textClickedTimes == 5){
            onTextClick()
            textClickedTimes = 0
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {


        val textState = remember {
            mutableStateOf(TextFieldValue(""))
        }

        SearchView(
            state = textState,
            placeHolder = "Поиск по названию", // a hint in the search field
            modifier = Modifier
        )

        val searchedText = textState.value.text

        val favoriteShares = allShares.value.filter {
            it.isFavorite == true

        }.filter {
            it.shortName.contains(searchedText, ignoreCase = true) ||
                    it.secId.contains(searchedText, ignoreCase = true)
        }

        val numberOfFavorite = favoriteShares.size

        val notFavoriteShares = allShares.value.filter {
            it.isFavorite == false
        }.filter {
            it.shortName.contains(searchedText, ignoreCase = true) ||
                    it.secId.contains(searchedText, ignoreCase = true)
        }

        favoriteShares.forEachIndexed { index, it ->

            BaseShareRow(
                index = index + 1,
                onHeartClick = { secId, isLiked ->
                    ShareScreenViewModel.changeShareIsFavoriteStatus(secId, isLiked)
                },
                share = it,
                onShareClick = { onShareClick(it.secId) },
                modifier = Modifier.heightIn(min = 70.dp, max = 70.dp)
            )

            AppDivider()
        }


        notFavoriteShares.forEachIndexed { index, it ->
            BaseShareRow(
                index = index + 1 + numberOfFavorite,
                onHeartClick = { secId, isLiked ->
                    ShareScreenViewModel.changeShareIsFavoriteStatus(secId, isLiked)
                },
                share = it,
                onShareClick = { onShareClick(it.secId) },
                modifier = Modifier.heightIn(min = 70.dp, max = 70.dp)
            )

            AppDivider()

        }

        Spacer(modifier = Modifier.height(15.dp))
        Text(text = textOfUpdate, modifier = Modifier.clickable {
            textClickedTimes++
            if (textClickedTimes == 5){
                onTextClick()
                textClickedTimes =0
            }
        })
        Spacer(modifier = Modifier.height(15.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    // that's just a hint inside of the search bar
    placeHolder: String,
    modifier: Modifier,
) {

    OutlinedTextField(
        value = state.value,
        onValueChange = {
            state.value = it

        },
        modifier
            .fillMaxWidth()

            .clip(RoundedCornerShape(6.dp))
            .border(1.dp, Color.DarkGray, RoundedCornerShape(6.dp)),
        placeholder = {
            Text(
                text = placeHolder,
                color = bright_brown_color
            )
        },
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(
            color = Color.White, fontSize = 20.sp
        )
    )
}


@Composable
private fun BaseShareRow(
    index: Int,
    modifier: Modifier = Modifier,
    share: ShareEntity,
    onShareClick: () -> Unit = {},
    onHeartClick: (secId: String, isLiked: Boolean) -> Unit,
) {

    val isLiked = share.isFavorite
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(

            modifier = modifier
                .weight(1f)
                .clickable {
                    onShareClick()
                },
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center

        ) {

            Text(
                text = "$index. ${share.secId}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                // color = bluish
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = share.shortName,
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )

        }
        val icon: ImageVector
        if (isLiked) {
            icon = Icons.Filled.Favorite
        } else {
            icon = Icons.Filled.FavoriteBorder
        }

        Row(
            modifier = modifier
                .weight(0.3f)
                .clickable {
                    onHeartClick(share.secId, !share.isFavorite)
                },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {


            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp),
            )

        }

    }
}




