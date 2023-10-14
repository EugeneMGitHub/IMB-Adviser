package com.example.shareappsettingswithgiraffe.ui.screens.infoScreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shareappsettingswithgiraffe.navigation.Routes
import com.example.shareappsettingswithgiraffe.navigation.mainTopBarTabs
import com.example.shareappsettingswithgiraffe.ui.components.MainTopBar
import com.example.shareappsettingswithgiraffe.ui.theme.bluish
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    secId: String?,
    onPopBackStack: () -> Unit = {},
    infoScreenViewModel: infoScreenViewModel,
    onHeartClick: (secId: String, isLiked: Boolean) -> Unit,
) {


    val shareInfoItem = infoScreenViewModel.infoItemVM
    shareInfoItem.secId = secId!!

    val allHistory by infoScreenViewModel.allHistory1.collectAsState()
    val partialHistory by infoScreenViewModel.partialHistory.collectAsState()
    val listOfAllShares =
        infoScreenViewModel.listOfAllShares.collectAsState(initial = emptyList()).value
    val listOfDatesForGraph = infoScreenViewModel.listOfDatesForGraph.collectAsState().value




    LaunchedEffect(key1 = Unit) {
        launch {
            infoScreenViewModel.getHistoryList(secId)
        }
        launch {
            infoScreenViewModel.getShareAdditionalInfo(secId)
        }
        launch {
            infoScreenViewModel.getListOfDatesForTheGraph(InfoScreenEvents.OnFiveYearsButtonClick)
        }
    }

    // Listener for button are being clicked  (for the Graphic)
    LaunchedEffect(key1 = true) {
        infoScreenViewModel.infoScreenUiEvent.collect { infoScreenEvent ->
            when (infoScreenEvent) {
                is InfoScreenEvents.OnWeekButtonClick -> {
                    infoScreenViewModel.getPartialHistory(7)
                    infoScreenViewModel.getListOfDatesForTheGraph(infoScreenEvent)
                }

                is InfoScreenEvents.OnMonthButtonClick -> {
                    infoScreenViewModel.getPartialHistory(31)
                    infoScreenViewModel.getListOfDatesForTheGraph(infoScreenEvent)
                }

                is InfoScreenEvents.OnSixMonthButtonClick -> {
                    infoScreenViewModel.getPartialHistory(31 * 6)
                    infoScreenViewModel.getListOfDatesForTheGraph(infoScreenEvent)
                }

                is InfoScreenEvents.OnYearButtonClick -> {
                    infoScreenViewModel.getPartialHistory(31 * 12)
                    infoScreenViewModel.getListOfDatesForTheGraph(infoScreenEvent)
                }

                is InfoScreenEvents.OnFiveYearsButtonClick -> {
                    infoScreenViewModel.partialHistoryEqualAllHistory()
                    infoScreenViewModel.getListOfDatesForTheGraph(infoScreenEvent)
                }

                is InfoScreenEvents.onBackButtonClick -> {
                    onPopBackStack()
                }
            }

        }
    }


    var isFavorite: Boolean = false
    var shortName = ""


    if (listOfAllShares.isNotEmpty()) {
        val theShare = listOfAllShares.find { it.secId == secId }
        isFavorite = theShare!!.isFavorite
        shortName = theShare.shortName
    }




    Scaffold(
        topBar = {
            ShareNameWithBackButton(
                secId = secId,
                isFavorite = isFavorite,
                shareName = shortName,
                navigateUp = { infoScreenViewModel.onEvent(InfoScreenEvents.onBackButtonClick) },
                onHeartClick = onHeartClick,
            )


        },
        modifier = Modifier.fillMaxSize(), // Fill the entire screen
    )
    {
        val Defaultpadding = it.calculateLeftPadding(LayoutDirection.Ltr)

        // val buttonShape = RoundedCornerShape(10.dp)
        // val buttonMinWidth = 120.dp
        val SCREEN_PADDIN = 12.dp

        Column(
            modifier = Modifier
                .padding(start = SCREEN_PADDIN, end = SCREEN_PADDIN, top = it.calculateTopPadding())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            if (partialHistory != emptyList<Float>()) {
                println("listOfDatesForGraph: is $listOfDatesForGraph")
                DrawGraphItself(
                    graphColor = MaterialTheme.colorScheme.primary,
                    // graphColor = bluish,
                    dataList = partialHistory,
                    listOfDatesRaw = listOfDatesForGraph
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            InfoScreenButtons(infoScreenViewModel = infoScreenViewModel)

            InfoScreenSharesInfo(shareInfoItem = shareInfoItem)

        }

    }

}