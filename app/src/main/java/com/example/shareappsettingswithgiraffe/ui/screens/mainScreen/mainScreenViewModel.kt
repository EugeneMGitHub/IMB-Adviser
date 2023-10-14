package com.example.shareappsettingswithgiraffe.ui.screens.mainScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shareappsettingswithgiraffe.data.database.DataBaseRepository
import com.example.shareappsettingswithgiraffe.data.database.NewsUpdateStrategy
import com.example.shareappsettingswithgiraffe.network.ApiRepository
import com.example.shareappsettingswithgiraffe.utils.StrategyOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class mainScreenViewModel @Inject constructor(
    private val dataBaseRepository: DataBaseRepository,
    private val repository: ApiRepository,
) : ViewModel() {


    val naborCennihBumagOptions =
        listOf(StrategyOptions.SharesType.ALL_SHARES, StrategyOptions.SharesType.ONLY_FAVORITE)
    val strategiaOptions = listOf(
        StrategyOptions.ChangeDirection.INCREASING, StrategyOptions.ChangeDirection.DECREASING
    )
    val objectSravneniaOptions = listOf(
        StrategyOptions.СomparisonTarget.MAX_PRICE,
        StrategyOptions.СomparisonTarget.MIN_PRICE,
        StrategyOptions.СomparisonTarget.START_OF_THE_PERIOD,
    )
    val periodSravneniaOptions = listOf(
        StrategyOptions.ChangePeriod.ONE_WEEK,
        StrategyOptions.ChangePeriod.ONE_MONTH,
        StrategyOptions.ChangePeriod.THREE_MONTH,
        StrategyOptions.ChangePeriod.SIX_MONTH,
        StrategyOptions.ChangePeriod.ONE_YEAR,
        StrategyOptions.ChangePeriod.THREE_YEARS,
        StrategyOptions.ChangePeriod.FIVE_YEARS,
    )


    private val _naborCennihBumagValueStateFlow =
        MutableStateFlow<String>(StrategyOptions.SharesType.ALL_SHARES)
    val naborCennihBumagValueStateFlow = _naborCennihBumagValueStateFlow.asStateFlow()

    var naborCennihBumagValue by mutableStateOf(StrategyOptions.SharesType.ALL_SHARES)
        private set
    var strategiaValue by mutableStateOf(StrategyOptions.ChangeDirection.INCREASING)
        private set
    var objectSravneniaValue by mutableStateOf(StrategyOptions.СomparisonTarget.MAX_PRICE)
        private set
    var periodSravneniaValue by mutableStateOf(StrategyOptions.ChangePeriod.THREE_YEARS)
        private set
    var porogIzmeneniaValue by mutableStateOf("0.0")
        private set


    fun changenaborCennihBumagValue(newValue: String) {
        naborCennihBumagValue = newValue
        _naborCennihBumagValueStateFlow.value = newValue
    }

    fun changeStrategiaValue(newValue: String) {
        strategiaValue = newValue
    }

    fun changeObjectSravneniaValue(newValue: String) {
        objectSravneniaValue = newValue
    }

    fun changePeriodSravneniaValue(newValue: String) {
        periodSravneniaValue = newValue
    }

    fun changePorogIzmeneniaValue(newValue: String?) {

        var valueToWrite = ""

        if (newValue == null) {
            valueToWrite = ""
        } else {
            valueToWrite = newValue
        }

        porogIzmeneniaValue = valueToWrite

    }


    suspend fun deleteSearchStrategy() {
        dataBaseRepository.deleteSearchStrategy()
    }


    suspend fun LoadConditionsForStrategyFromDB() {

        val searchStrategy: NewsUpdateStrategy? =
            dataBaseRepository.getSearchStrategyById(strategyId = 1)

        println("mainScreenVM: searchStrategy from database is $searchStrategy")

        if (searchStrategy != null) {
            val sharesType = searchStrategy.sharesType
            val changeDirection = searchStrategy.changeDirection
            val comparisonTarget = searchStrategy.comparisonTarget
            val changePeriod = searchStrategy.changePeriod
            val changeThresholdPercent: Float = searchStrategy.changeThresholdPercent

            changenaborCennihBumagValue(sharesType)
            changeStrategiaValue(changeDirection)
            changeObjectSravneniaValue(comparisonTarget)
            changePeriodSravneniaValue(changePeriod)
            changePorogIzmeneniaValue(changeThresholdPercent.toString())
        }

    }


    fun insertDefaultStrategy() {
        viewModelScope.launch {
            val listOfStrategies = dataBaseRepository.getAllSearchStrategy()
            println("mainScreenVM: All searchStrategies are: $listOfStrategies")
            val SearchStrategy: NewsUpdateStrategy = NewsUpdateStrategy(
                strategyId = 1,
                sharesType = StrategyOptions.SharesType.ALL_SHARES,
                changeDirection = StrategyOptions.ChangeDirection.INCREASING,
                comparisonTarget = StrategyOptions.СomparisonTarget.MIN_PRICE,
                changePeriod = StrategyOptions.ChangePeriod.ONE_YEAR,
                changeThresholdPercent = StrategyOptions.ChangeThresholdPercent.DEFAULT_PERCENT
            )
            println("mainScreenVM: default searchStrategy is: $SearchStrategy")
            dataBaseRepository.insertSearchStrategy(SearchStrategy)
            val SearchStrategyFromDb = dataBaseRepository.getSearchStrategyById(strategyId = 1)
            println("mainScreenVM: Search strategy from DB is: $SearchStrategyFromDb")
        }
    }


    fun updateSearchStrategy() {
        viewModelScope.launch {

            var porogIzmeneniaValueNotNull = porogIzmeneniaValue


            if (porogIzmeneniaValue.isEmpty()) {
                porogIzmeneniaValueNotNull = "0.1"
            } else {
                porogIzmeneniaValueNotNull = porogIzmeneniaValue
            }


            if (porogIzmeneniaValue.contains(",")) {
                porogIzmeneniaValueNotNull = porogIzmeneniaValue.replace(',', '.')
            }

            try {
                porogIzmeneniaValueNotNull.toFloat()
                println("porogIzmeneniaValueNotNull: can be parsed as a Float")
            } catch (e: NumberFormatException) {
                println("porogIzmeneniaValueNotNull: can NOT be parsed as a Float")
                porogIzmeneniaValueNotNull = "0.1"
            }

            porogIzmeneniaValue = porogIzmeneniaValueNotNull

            val existingStrategy = dataBaseRepository.getSearchStrategyById(strategyId = 1)
            existingStrategy.let {
                val updatedStrategy = it.copy(
                    sharesType = naborCennihBumagValue,
                    changeDirection = strategiaValue,
                    comparisonTarget = objectSravneniaValue,
                    changePeriod = periodSravneniaValue,
                    changeThresholdPercent = porogIzmeneniaValueNotNull.toFloat()
                )

                dataBaseRepository.updateSearchStrategy(updatedStrategy)

            }
        }
    }


}