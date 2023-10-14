package com.example.shareappsettingswithgiraffe.ui.screens.infoScreen

data class DetailedShareInfo(
    var secId: String,
    var shortName: String = "",
    var fullName: String = "",
    var prevPrice: Double = 0.0,
    var openPrice: Double? = 0.0,
    var lastPrice: Double? = 0.0,
    var marketPrice: Double? = 0.0,
    var lastDealTime: String = ""
)