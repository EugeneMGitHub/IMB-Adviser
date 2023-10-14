package com.example.shareappsettingswithgiraffe.ui.screens.newsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shareappsettingswithgiraffe.data.database.DataBaseRepository
import com.example.shareappsettingswithgiraffe.data.database.NewsUpdateStrategy
import com.example.shareappsettingswithgiraffe.data.database.ShareEntity
import com.example.shareappsettingswithgiraffe.data.database.ShareHistory
import com.example.shareappsettingswithgiraffe.data.database.ShareNews
import com.example.shareappsettingswithgiraffe.network.ApiRepository
import com.example.shareappsettingswithgiraffe.utils.StrategyOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


@HiltViewModel
class NewsScreenViewModel @Inject constructor(
    private val dataBaseRepository: DataBaseRepository,
    private val repository: ApiRepository,
) : ViewModel() {


    val listOfNews1 = dataBaseRepository.getAllNews()
    val listOfAllShares = dataBaseRepository.getAllShares()


    suspend fun getShareBySecId(secId: String): ShareEntity {
        return dataBaseRepository.getShareBySecId(secId)
    }


    suspend fun DeleteAllTheNews(){
        dataBaseRepository.deleteAllNews()
    }

    suspend fun CalculateTheNews() {

        dataBaseRepository.deleteAllNews()

        val searchStrategy: NewsUpdateStrategy =
            dataBaseRepository.getSearchStrategyById(strategyId = 1)
        println("NewsScreenVM: searchStrategy = $searchStrategy")

        val sharesType = searchStrategy.sharesType
        val changeDirectionFromDB = searchStrategy.changeDirection
        val comparisonTargetFromDB = searchStrategy.comparisonTarget
        val changePeriodFromDB = searchStrategy.changePeriod

        val changeThresholdPercentFromDB: Float = searchStrategy.changeThresholdPercent

        var nDays = 0
        when (changePeriodFromDB) {
            StrategyOptions.ChangePeriod.ONE_WEEK -> nDays = 7
            StrategyOptions.ChangePeriod.ONE_MONTH -> nDays = 31
            StrategyOptions.ChangePeriod.THREE_MONTH -> nDays = 31 * 3
            StrategyOptions.ChangePeriod.SIX_MONTH -> nDays = 31 * 6
            StrategyOptions.ChangePeriod.ONE_YEAR -> nDays = 365
            StrategyOptions.ChangePeriod.THREE_YEARS -> nDays = 365 * 3
            StrategyOptions.ChangePeriod.FIVE_YEARS -> nDays = 365 * 5
        }

        var isOnlyFavoriteShares = false
        when (sharesType) {
            StrategyOptions.SharesType.ALL_SHARES -> {
                isOnlyFavoriteShares = false
            }

            StrategyOptions.SharesType.ONLY_FAVORITE -> {
                isOnlyFavoriteShares = true
            }
        }

        var conditionUp: Boolean = true
        when (changeDirectionFromDB) {
            StrategyOptions.ChangeDirection.INCREASING -> {
                conditionUp = true
            }

            StrategyOptions.ChangeDirection.DECREASING -> {
                conditionUp = false
            }
        }



        println("NewsScreenVM: isOnlyFavoriteShares = $isOnlyFavoriteShares, nDays = $nDays,  conditionUp = $conditionUp, ")


        var listOfSharesToWorkWith = emptyList<ShareEntity>()

        viewModelScope.async {

            if (isOnlyFavoriteShares) {
                listOfSharesToWorkWith =
                    dataBaseRepository.getAllSharesByIsFavoriteWithoutFlow(isOnlyFavoriteShares)
            } else {
                listOfSharesToWorkWith = dataBaseRepository.getAllSharesWithoutFlow()
            }

        }.await()



        println("NewsScreenVM: listOfSharesToWorkWith has ${listOfSharesToWorkWith.size} items and consists of $listOfSharesToWorkWith")

        listOfSharesToWorkWith.forEach {
            val allSH = dataBaseRepository.getAllHistoryWithoutFlow(it.secId)
            println("NewsScreenVM: For ${it.secId} history is: $allSH")
        }

        CreateNews(
            nDays = nDays,
            conditionUp = conditionUp,
            listOfSharesToWorkWith = listOfSharesToWorkWith,
            changeDirectionFromDB = changeDirectionFromDB,
            comparisonTargetFromDB = comparisonTargetFromDB,
            changePeriodFromDB = changePeriodFromDB,
            changeThresholdPercentFromDB = changeThresholdPercentFromDB
        )

    }


    suspend fun CreateNews(
        nDays: Int,
        conditionUp: Boolean,
        listOfSharesToWorkWith: List<ShareEntity>,
        changeDirectionFromDB: String,
        comparisonTargetFromDB: String,
        changePeriodFromDB: String,
        changeThresholdPercentFromDB: Float,
    ) {

        listOfSharesToWorkWith.forEach { Share ->

            viewModelScope.launch {

                val shareSecId = Share.secId

                val openPriceAsync = async { GetOpenPrice(shareSecId = shareSecId) }
                println("NewsScreenVM: For = $shareSecId openPriceAsync is ${openPriceAsync.await()} ")

                val comparisonTargetAsync = async {
                    GetComparisonTarget(
                        shareSecId = shareSecId,
                        nDays = nDays,
                        comparisonTargetFromDB = comparisonTargetFromDB
                    )
                }
                println("NewsScreenVM: For = $shareSecId comparisonTargetAsync is ${comparisonTargetAsync.await()} ")


                val realChangePercent = (openPriceAsync.await() - comparisonTargetAsync.await()
                    .toFloat()) / comparisonTargetAsync.await().toFloat() * 100

                var opnPrice = openPriceAsync.await().toDouble()
                opnPrice = ((opnPrice * 100.0).roundToInt() / 100.0)

//                    println("NewsScreenVM: For = $shareSecId comparisonTargetAsync is ${comparisonTargetAsync.await()}, openPriceAsync is ${openPriceAsync.await()} and realChangePercent is $realChangePercent")
                val realChangePercentAbsolute = realChangePercent.absoluteValue
                println("NewsScreenVM: For = $shareSecId comparisonTargetAsync is ${comparisonTargetAsync.await()}, openPriceAsync is ${openPriceAsync.await()} and realChangePercent is $realChangePercent")

                if (realChangePercentAbsolute >= changeThresholdPercentFromDB) {

                    val signOfChange =
                        (openPriceAsync.await() - comparisonTargetAsync.await().toFloat()) >= 0
                    // in case conditionUp is true
                    if (conditionUp == signOfChange) {
                        println("NewsScreenVM: seciD = $shareSecId has realChangePercent equal to $realChangePercent")
                        val shareNews: ShareNews = ShareNews(
                            changeDirection = changeDirectionFromDB,
                            changePercent = realChangePercent.toString(),
                            changePeriod = changePeriodFromDB,
                            comparisonTarget = comparisonTargetFromDB,
                            secId = shareSecId,
                            currentPrice = opnPrice
                        )

                        dataBaseRepository.insertNews(shareNews)
                    } else {
                        Unit
                    }
                } else {
                    Unit
                }


            }
        }

    }


    private suspend fun GetOpenPrice(shareSecId: String): Float {
        var openPrice = 0.0f
        repository.getShareAdditionalInfo(shareSecId).let { Response ->
            if (Response.isSuccessful) {
                var prevPrice = 0.0
                Response.body()?.securities?.data?.get(0).let { it: List<Any>? ->
                    if (it?.get(5) != null) {
                        prevPrice = it?.get(5) as Double
                    }
                    prevPrice = it?.get(5) as Double
                    // println("NewsScreenVM: For $shareSecId prevPrice is $prevPrice")
                }
                var lastPrice = 0.0
                var marketPrice = 0.0
                var openPriceFromApi = 0.0
                Response.body()?.marketdata?.data?.get(0).let { it: List<Any>? ->

                    // println("NewsScreenVM: For $shareSecId marketdata?.data? is ${Response.body()?.marketdata?.data}")
                    if (it?.get(0) != null) {
                        lastPrice = it?.get(0) as Double
                    } else {
                        // println("NewsScreenVM: For $shareSecId lastPrice is null ")
                    }

                    if (it?.get(2) != null) {
                        openPriceFromApi = it?.get(2) as Double
                    } else {
                        // println("NewsScreenVM: For $shareSecId openPriceFromApi is null ")
                    }

                    if (it?.get(4) != null) {
                        marketPrice = it?.get(4) as Double
                    } else {
                        // println("NewsScreenVM: For $shareSecId marketPrice is null ")
                    }

                    if (openPriceFromApi != 0.0) {
                        openPrice = openPriceFromApi.toFloat()
                    } else {
                        if (lastPrice != 0.0) {
                            openPrice = lastPrice.toFloat()
                        } else {
                            openPrice = marketPrice.toFloat()
                        }
                    }

                    if (openPrice == 0.0.toFloat()) {
                        openPrice = prevPrice.toFloat()
                    }

                    //  println("NewsScreenVM: For $shareSecId openPrice is $openPrice ")

                }

            }
        }
        return openPrice
    }


    private suspend fun GetComparisonTarget(
        shareSecId: String,
        nDays: Int,
        comparisonTargetFromDB: String
    ): Double {
        var comparisonTarget: Double = 0.0

        dataBaseRepository.getAllHistoryWithoutFlow(shareSecId).let { historyList ->

            val sublist: List<ShareHistory>
            if (nDays != 365 * 5) {
                sublist = historyList.filter {
                    it.historyClosePrice != 0.0
                }.takeLast(nDays)

            } else {
                sublist = historyList.filter {
                    it.historyClosePrice != 0.0
                }
            }

            when (comparisonTargetFromDB) {
                StrategyOptions.СomparisonTarget.MAX_PRICE -> {
                    comparisonTarget = sublist.maxBy {
                        it.historyClosePrice
                    }.historyClosePrice
                }

                StrategyOptions.СomparisonTarget.MIN_PRICE -> {


                    comparisonTarget = sublist.minBy {
                        it.historyClosePrice
                    }.historyClosePrice
                }

                StrategyOptions.СomparisonTarget.START_OF_THE_PERIOD -> {
                    comparisonTarget = sublist[0].historyClosePrice
                }
            }
        }
        return comparisonTarget
    }

}



