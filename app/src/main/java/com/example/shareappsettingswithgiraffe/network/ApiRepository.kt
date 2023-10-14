package com.example.shareappsettingswithgiraffe.network

import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService) {


    //Checking if MB Works today
    suspend fun getTheLastDealTime() = apiService.getTheLastDealTime()

    // Get the list of share from Moz Birz with secId and shortNames
    suspend fun getTheListOfSharesOfMBWithSecIdAndShortName() =
        apiService.getTheListOfSharesOfMBWithSecIdAndShortName()

    // Get the last and Open values for a specific SecID
    suspend fun getLastAndOpenWithInput(secId: String) = apiService.getLastAndOpenWithInput(secId)

    // Get hostory for a specific SecID and Date range
    suspend fun getHistoryWithSecIdAndDataRange(
        secId: String,
        dateRangeStart: String,
        dateRangeEnd: String
    ) =
        apiService.getHistoryWithSecIdAndDataRange(
            secId = secId,
            dateRangeStart = dateRangeStart,
            dateRangeEnd = dateRangeEnd
        )


    suspend fun getShareAdditionalInfo(secId: String) = apiService.getInfoForShareScreen(secId)


}