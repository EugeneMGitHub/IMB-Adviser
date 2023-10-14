package com.example.shareappsettingswithgiraffe.ui.screens.infoScreen

sealed class InfoScreenEvents {

    object OnWeekButtonClick : InfoScreenEvents()
    object OnMonthButtonClick : InfoScreenEvents()
    object OnSixMonthButtonClick : InfoScreenEvents()
    object OnYearButtonClick : InfoScreenEvents()
    object OnFiveYearsButtonClick : InfoScreenEvents()

    object onBackButtonClick : InfoScreenEvents()

    //   data class OnshowShanckBarButtonClicked(
    //        val message: String,
    //        val action: String? = null
    //    ) : RetrofitScreenEvents()

}