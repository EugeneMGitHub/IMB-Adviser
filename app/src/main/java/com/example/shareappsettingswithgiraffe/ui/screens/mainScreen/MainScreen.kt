package com.example.shareappsettingswithgiraffe.ui.screens.mainScreen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem


import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.Dp

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.DataUpdateUIState
import com.example.shareappsettingswithgiraffe.MainViewModel
import com.example.shareappsettingswithgiraffe.ui.components.AppDivider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainScreenViewModel: mainScreenViewModel,
    mainViewModel: MainViewModel,
    snackbarHostState: SnackbarHostState,
    onUpdateNewsBtnClickedOn: () -> Unit = {}
) {
    val isDataUploading = mainViewModel.downLoadingState.collectAsState(initial = DataUpdateUIState.IsLoading)
    val coroutineScope = rememberCoroutineScope()
    val showBtnToGetNews = rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        mainScreenViewModel.LoadConditionsForStrategyFromDB()
    }

    val SCREEN_PADDING = 12.dp


    Column(
        modifier = Modifier
            .padding(start = SCREEN_PADDING, end = SCREEN_PADDING)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val spacerHeightTop = 15.dp
        val spacerHeightBottom = 10.dp
        val alphaForHeadlines = Modifier.alpha(0.5f)
        val textStyle = MaterialTheme.typography.bodyLarge
        val headLineSize = 14.sp


        Text(
            "Параметры стратегии отбора акций:",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 10.dp, bottom = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Тип акций:",
            modifier = alphaForHeadlines
                .align(Alignment.Start)
                .padding(all = 0.dp),
            style = textStyle,
            fontSize = headLineSize
        )

        DropDownSpinner(
            itemList = mainScreenViewModel.naborCennihBumagOptions,
            selectedItem = mainScreenViewModel.naborCennihBumagValue,
            defaultText = "Выберите список акций для сравнения",
            onItemSelected = { index, item ->
                mainScreenViewModel.changenaborCennihBumagValue(item)
                mainScreenViewModel.updateSearchStrategy()

                ShowSnackBarRelativeToUploadingState(
                    isDataUploading = isDataUploading,
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                    showBtnToGetNews = showBtnToGetNews
                )
            },
        )

        AppDivider(modifier = Modifier.padding(top = spacerHeightTop, bottom = spacerHeightBottom))

        Text(
            "Cтратегия сравнения",
            modifier = alphaForHeadlines
                .align(Alignment.Start)
                .padding(all = 0.dp),
            style = textStyle,
            fontSize = headLineSize
        )

        DropDownSpinner(
            itemList = mainScreenViewModel.strategiaOptions,
            selectedItem = mainScreenViewModel.strategiaValue,
            defaultText = "Выберите cтратегию сравнения",
            onItemSelected = { index, item ->
                mainScreenViewModel.changeStrategiaValue(item)
                mainScreenViewModel.updateSearchStrategy()

                ShowSnackBarRelativeToUploadingState(
                    isDataUploading = isDataUploading,
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                    showBtnToGetNews = showBtnToGetNews
                )
            },
        )

        AppDivider(modifier = Modifier.padding(top = spacerHeightTop, bottom = spacerHeightBottom))

        Text(
            "Объект сравнения:",
            modifier = alphaForHeadlines
                .align(Alignment.Start)
                .padding(all = 0.dp),
            style = textStyle,
            fontSize = headLineSize
        )
        DropDownSpinner(
            itemList = mainScreenViewModel.objectSravneniaOptions,
            selectedItem = mainScreenViewModel.objectSravneniaValue,
            defaultText = "Выберите объект сравнения",
            onItemSelected = { index, item ->
                mainScreenViewModel.changeObjectSravneniaValue(item)
                mainScreenViewModel.updateSearchStrategy()

                ShowSnackBarRelativeToUploadingState(
                    isDataUploading = isDataUploading,
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                    showBtnToGetNews = showBtnToGetNews
                )
            },
        )

        AppDivider(modifier = Modifier.padding(top = spacerHeightTop, bottom = spacerHeightBottom))

        Text(
            "Период сравнения:",
            modifier = alphaForHeadlines
                .align(Alignment.Start)
                .padding(all = 0.dp),
            style = textStyle,
            fontSize = headLineSize
        )
        DropDownSpinner(
            itemList = mainScreenViewModel.periodSravneniaOptions,
            selectedItem = mainScreenViewModel.periodSravneniaValue,
            defaultText = "Выберите период сравнения",
            onItemSelected = { index, item ->
                mainScreenViewModel.changePeriodSravneniaValue(item)
                mainScreenViewModel.updateSearchStrategy()

                ShowSnackBarRelativeToUploadingState(
                    isDataUploading = isDataUploading,
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                    showBtnToGetNews = showBtnToGetNews
                )
            },

            )

        AppDivider(modifier = Modifier.padding(top = spacerHeightTop, bottom = spacerHeightBottom))


        val focusManager = LocalFocusManager.current


        OutlinedTextField(
            value = mainScreenViewModel.porogIzmeneniaValue,
            onValueChange = {
                mainScreenViewModel.changePorogIzmeneniaValue(it)
            },
            label = { Text(text = "Порог изменения, %") },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {

                    mainScreenViewModel.updateSearchStrategy()
                    focusManager.clearFocus()

                    ShowSnackBarRelativeToUploadingState(
                        isDataUploading = isDataUploading,
                        snackbarHostState = snackbarHostState,
                        coroutineScope = coroutineScope,
                        showBtnToGetNews = showBtnToGetNews
                    )

                }
            ),
        )



        Spacer(modifier = Modifier.height(10.dp))

        if (showBtnToGetNews.value == false) {
            Text(
                text = "*Рекомендации согласно данной стратегии можно посмотреть в окне Новости",
                textAlign = TextAlign.Start,
                lineHeight = 15.sp
            )
        }

        if (showBtnToGetNews.value == true) {

            Button(
                colors = ButtonDefaults.buttonColors(),
                onClick = {
                    onUpdateNewsBtnClickedOn()
                    coroutineScope.launch {
                        val snackBarResult = snackbarHostState.showSnackbar(
                            message = "Новая стратегия обрабтывается",
                            actionLabel = "Понятно",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                        when (snackBarResult) {
                            androidx.compose.material3.SnackbarResult.Dismissed -> {} // actions when action label wasn't clicked on
                            androidx.compose.material3.SnackbarResult.ActionPerformed -> {} // actions when action label was actually clicked on
                        }
                    }
                    showBtnToGetNews.value = false
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10)
            ) {
                Text("Получить новые рекомендации")
            }
        }
    }
}


fun showSnackBarForAnyStrategyChange(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
) {

    coroutineScope.launch {
        val snackBarResult = snackbarHostState.showSnackbar(
            message = "Стратегия обновлена",
            actionLabel = "Понятно",
            duration = androidx.compose.material3.SnackbarDuration.Short
        )

        when (snackBarResult) {
            androidx.compose.material3.SnackbarResult.ActionPerformed -> {
            } // actions when action label wasn't clicked on
            androidx.compose.material3.SnackbarResult.Dismissed -> {
            } // actions when action label was actually clicked on

        }
    }

}

fun showSnackBarIsUploding(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
) {

    coroutineScope.launch {
        val snackBarResult = snackbarHostState.showSnackbar(
            message = "Стратегия обновлена, но данные еще загружаются.",
            actionLabel = "Понятно",
            duration = androidx.compose.material3.SnackbarDuration.Short
        )

        when (snackBarResult) {
            androidx.compose.material3.SnackbarResult.ActionPerformed -> {
            } // actions when action label wasn't clicked on
            androidx.compose.material3.SnackbarResult.Dismissed -> {
            } // actions when action label was actually clicked on

        }
    }

}

fun ShowSnackBarRelativeToUploadingState(
    isDataUploading: State<DataUpdateUIState>,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    showBtnToGetNews: MutableState<Boolean>,
) {
    when (isDataUploading.value) {

        DataUpdateUIState.IsLoading -> {
            showSnackBarIsUploding(
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope,
            )
        }

        DataUpdateUIState.LoadingHasCompleted -> {
            showSnackBarForAnyStrategyChange(
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope,
            )

            showBtnToGetNews.value = true
        }
    }
}





