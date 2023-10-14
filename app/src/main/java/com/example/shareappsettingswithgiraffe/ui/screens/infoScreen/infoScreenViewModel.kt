package com.example.shareappsettingswithgiraffe.ui.screens.infoScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shareappsettingswithgiraffe.data.database.DataBaseRepository
import com.example.shareappsettingswithgiraffe.models.shareHistory.History
import com.example.shareappsettingswithgiraffe.network.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class infoScreenViewModel @Inject constructor(
    private val dataBaseRepository: DataBaseRepository,
    private val repository: ApiRepository,
) : ViewModel() {


    val listOfAllShares = dataBaseRepository.getAllShares()

    var infoItemVM by mutableStateOf(DetailedShareInfo(secId = ""))
        private set


    private var _allHistory1 = MutableStateFlow<List<Float>>(emptyList())
    val allHistory1 = _allHistory1.asStateFlow()

    private var _partialHistory = MutableStateFlow<List<Float>>(emptyList())
    val partialHistory = _partialHistory.asStateFlow()


    private var _listOfDatesForGraph = MutableStateFlow<List<String>>(emptyList())
    val listOfDatesForGraph = _listOfDatesForGraph.asStateFlow()


    var delFunction = ""
    var delFunctionWithMSinVM by mutableStateOf("")


//    var justVal by mutableStateOf("")
//        private set

    private val _infoScreenUiEvent = Channel<InfoScreenEvents>()
    val infoScreenUiEvent = _infoScreenUiEvent.receiveAsFlow()

    fun onEvent(event: InfoScreenEvents) {
        when (event) {
            is InfoScreenEvents.OnWeekButtonClick -> {
                viewModelScope.launch { _infoScreenUiEvent.send(event) }
            }

            is InfoScreenEvents.OnMonthButtonClick -> {
                viewModelScope.launch { _infoScreenUiEvent.send(event) }
            }

            is InfoScreenEvents.OnSixMonthButtonClick -> {
                viewModelScope.launch { _infoScreenUiEvent.send(event) }
            }

            is InfoScreenEvents.OnYearButtonClick -> {
                viewModelScope.launch { _infoScreenUiEvent.send(event) }
            }

            is InfoScreenEvents.OnFiveYearsButtonClick -> {
                viewModelScope.launch { _infoScreenUiEvent.send(event) }
            }

            is InfoScreenEvents.onBackButtonClick -> {
                senUiEvent(event)
            }
        }
    }

    private fun senUiEvent(event: InfoScreenEvents) {
        viewModelScope.launch {
            _infoScreenUiEvent.send(event)
        }
    }


    fun getListOfDatesForTheGraph(event: InfoScreenEvents) {

        var listOfDates = emptyList<String>()
        //    val datePattern = DateTimeFormatter.ofPattern("dd.mm.yy")
        //    val offSetDateTime = LocalDate.parse("2023-03-08", datePattern)
        //    println("DrawGraphItself:  offSetDateTime is $offSetDateTime")

        //    val dataPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        //    val dataPattern = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        //    val currentDate1 = LocalDate.now().format(dataPattern)
        //    val startOfPeriod1 = currentDate.minusWeeks(1).format(dataPattern).toString()

        val dataPattern = DateTimeFormatter.ofPattern("dd.MM.yy")
        val currentDateFormated = LocalDate.now().format(dataPattern)
        println("currentDateFormated: currentDateFormated is $currentDateFormated")

        val currentDate333 = LocalDate.now().changeDatePatternToString()
        println("currentDate333: currentDate333 is $currentDate333")

        val currentDate = LocalDate.now()
        var startOfPeriod = currentDate

        when (event) {
            is InfoScreenEvents.OnWeekButtonClick -> {
                startOfPeriod = currentDate.minusWeeks(1)
                val secondValue = ""
                val thirdValue = currentDate.minusDays(3)
                val forthValue = ""
                listOfDates = listOf(
                    startOfPeriod.changeDatePatternToString(),
                    secondValue,
                    thirdValue.changeDatePatternToString(),
                    forthValue,
                    currentDate.changeDatePatternToString()
                )
                _listOfDatesForGraph.value = listOfDates
            }

            is InfoScreenEvents.OnMonthButtonClick -> {
                startOfPeriod = currentDate.minusMonths(1)
                val secondValue = currentDate.minusDays(23)
                val thirdValue = currentDate.minusDays(15)
                val forthValue = currentDate.minusDays(8)

                listOfDates =
                    listOf(startOfPeriod, secondValue, thirdValue, forthValue, currentDate).map {
                        it.changeDatePatternToString()
                    }
                _listOfDatesForGraph.value = listOfDates
            }

            is InfoScreenEvents.OnSixMonthButtonClick -> {
                startOfPeriod = currentDate.minusMonths(6)
                val secondValue = currentDate.minusDays(18 * 3)
                val thirdValue = currentDate.minusDays(18 * 2)
                val forthValue = currentDate.minusDays(18)
                listOfDates =
                    listOf(startOfPeriod, secondValue, thirdValue, forthValue, currentDate).map {
                        it.changeDatePatternToString()
                    }
                _listOfDatesForGraph.value = listOfDates
            }

            is InfoScreenEvents.OnYearButtonClick -> {
                startOfPeriod = currentDate.minusYears(1)
                val secondValue = currentDate.minusDays(93 * 3)
                val thirdValue = currentDate.minusDays(93 * 2)
                val forthValue = currentDate.minusDays(93)
                listOfDates =
                    listOf(startOfPeriod, secondValue, thirdValue, forthValue, currentDate).map {
                        it.changeDatePatternToString()
                    }
                _listOfDatesForGraph.value = listOfDates
            }

            is InfoScreenEvents.OnFiveYearsButtonClick -> {
                startOfPeriod = currentDate.minusYears(5)
                val secondValue = currentDate.minusDays(456 * 3)
                val thirdValue = currentDate.minusDays(456 * 2)
                val forthValue = currentDate.minusDays(456)
                listOfDates =
                    listOf(startOfPeriod, secondValue, thirdValue, forthValue, currentDate).map {
                        it.changeDatePatternToString()
                    }
                _listOfDatesForGraph.value = listOfDates
            }

            is InfoScreenEvents.onBackButtonClick -> {}
        }


        println("getListOfDatesForTheGraph: listOfDates is $listOfDates")
    }


    fun LocalDate.changeDatePatternToString(): String {
        val dataPattern = DateTimeFormatter.ofPattern("dd.MM.yy")
        return this.format(dataPattern).toString()
    }


    suspend fun getHistoryList(secId: String) {
        viewModelScope.launch {

            dataBaseRepository.getAllHistoryWithoutFlow(secId).let { historyList ->

                val historyClosePrices = historyList.map {
                    it.historyClosePrice.toFloat()
                }
                val historyClosePricesWithoutZeros = historyClosePrices.filter { it != 0.0f }
                _allHistory1.value = historyClosePricesWithoutZeros
                _partialHistory.value = historyClosePricesWithoutZeros
                println("getHistoryList: historyClosePrices = $historyClosePrices")
                println("getHistoryList: allHistory1 = ${allHistory1.value}")

            }
        }

    }

    fun getPartialHistory(n: Int) {
        val sizeOfAllHistory = allHistory1.value.size
        _partialHistory.value = allHistory1.value.takeLast(n)
    }

    fun partialHistoryEqualAllHistory() {
        _partialHistory.value = _allHistory1.value
    }


    suspend fun getShareAdditionalInfo(secId: String) {

        val infoItem = DetailedShareInfo(
            secId = secId
        )
        println("InfoScreenVM: infoItem is $infoItem")

        val secId = secId
        var shortName: String
        var fullName: String
        var prevPrice: Double

        var openPrice: Double?
        var lastPrice: Double?
        var marketPrice: Double?
        var lastDealTime: String

        viewModelScope.launch {
            repository.getShareAdditionalInfo(secId).let { Response ->
                if (Response.isSuccessful) {
                    println("InfoScreenVM: Response for additional info is successful")
                    println("InfoScreenVM: Response is ${Response.body()}")

                    Response.body()?.securities?.data?.get(0).let { it: List<Any>? ->

                        shortName = it?.getOrNull(1) as String
                        println("InfoScreenVM: shortName is ${shortName}")
                        fullName = it?.getOrNull(2) as String
                        println("InfoScreenVM: fullName is ${fullName}")
                        prevPrice = it?.getOrNull(5) as Double
                        println("InfoScreenVM: prevPrice is ${prevPrice}")

                        infoItem.shortName = shortName
                        infoItem.fullName = fullName
                        infoItem.prevPrice = prevPrice

                        println("InfoScreenVM: infoItem is $infoItem")
                    }

                    Response.body()?.marketdata?.data?.get(0).let {
                        lastPrice = it?.getOrNull(0) as Double
                        openPrice = it?.getOrNull(2) as Double
                        marketPrice = it?.getOrNull(4) as Double
                        lastDealTime = it?.getOrNull(5) as String


                        infoItem.lastPrice = lastPrice
                        infoItem.openPrice = openPrice
                        infoItem.marketPrice = marketPrice
                        infoItem.lastDealTime = lastDealTime

                    }

                    println("InfoScreenVM: infoItem is $infoItem")

                    infoItemVM = infoItem


                } else {
                    Log.d(
                        "InfoScreenVM",
                        "Failed to load Share Additional information: ${Response.errorBody()}"
                    )
                }

            }
        }

    }
}