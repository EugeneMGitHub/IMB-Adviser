package com.example.shareappsettingswithgiraffe.network

import com.example.shareappsettingswithgiraffe.models.IfWorkingHours.IfWorkingHours
import com.example.shareappsettingswithgiraffe.models.lastAndOpen.LastAndOpen
import com.example.shareappsettingswithgiraffe.models.shareAdditionalInfo.ShareAdditionalInfo
import com.example.shareappsettingswithgiraffe.models.shareHistory.ShareHistory
import com.example.shareappsettingswithgiraffe.models.sharesFromMB.SharesFromMbWithSecIdAndShortName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //Checking if MB Works today
    //iss/engines/stock/markets/shares/boards/TQBR/securities/SBER.json?iss.meta=off&iss.only=marketdata&marketdata.columns=TIME
    @GET("iss/engines/stock/markets/shares/boards/TQBR/securities/{SECID}.json")
    suspend fun getTheLastDealTime(
        @Path("SECID") secId: String = "SBER",
        @Query("iss.meta") meta: String = "off",
        @Query("iss.only") only: String = "marketdata",
        @Query("marketdata.columns") columns: String = "TIME"
    ): Response<IfWorkingHours>


    /**Gets the list of Shares from MosBirzha with secid and shortname*/
    @GET("iss/statistics/engines/stock/markets/index/analytics/IMOEX.json")
    suspend fun getTheListOfSharesOfMBWithSecIdAndShortName(
        @Query("iss.meta") meta: String = "off",
        @Query("analytics.columns") analyticsColumns: String = "ticker,shortnames",
        @Query("start") start: String = "0",
        @Query("limit") limit: String = "300"
    ): Response<SharesFromMbWithSecIdAndShortName>


    // https://iss.moex.com/iss/statistics/engines/stock/markets/index/analytics/IMOEX.json?iss.meta=off&analytics.columns=ticker,shortnames&start=0&limit=300 ?

    /**The function gets the current share value*/
    @GET("/iss/engines/stock/markets/shares/boards/TQBR/securities/{SECID}.json")
    suspend fun getLastAndOpenWithInput(
        @Path("SECID") secId: String,
        @Query("iss.meta") meta: String = "off",
        @Query("iss.only") only: String = "marketdata",
        @Query("marketdata.columns") marketDataColumns: String = "LAST, OPEN"
    ): Response<LastAndOpen>

    /**The function that gets history of a specific SECID and a date range*/
    @GET("iss/history/engines/stock/markets/shares/boards/TQBR/securities/{SECID}.json")
    suspend fun getHistoryWithSecIdAndDataRange(
        @Path("SECID") secId: String,
        @Query("from") dateRangeStart: String,
        @Query("till") dateRangeEnd: String,
        @Query("iss.meta") meta: String = "off",
        @Query("iss.json") json: String = "extended",
        @Query("iss.only") only: String = "history",
        @Query("history.columns") columns: String = "TRADEDATE,CLOSE"
    ): Response<ShareHistory>


    @GET("iss/engines/stock/markets/shares/boards/TQBR/securities/{SECID}.json")
    suspend fun getInfoForShareScreen(
        @Path("SECID") secId: String,
        @Query("iss.meta") meta: String = "off",
        @Query("iss.only") only: String = "securities,marketdata",
        @Query("marketdata.columns") marketDataColumns: String = "LAST,CHANGE,OPEN,SYSTIME,MARKETPRICE,TIME",
        @Query("securities.columns") securitiesColumns: String = "SECID,SHORTNAME,SECNAME,SETTLEDATE,PREVDATE,PREVPRICE"
    ): Response<ShareAdditionalInfo>


}