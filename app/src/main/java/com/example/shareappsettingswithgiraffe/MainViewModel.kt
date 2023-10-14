package com.example.shareappsettingswithgiraffe

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.DataUpdateUIState
import com.example.StagesOfUpdate
import com.example.shareappsettingswithgiraffe.data.database.DataBaseRepository
import com.example.shareappsettingswithgiraffe.data.database.FirstLoad
import com.example.shareappsettingswithgiraffe.data.database.ShareEntity
import com.example.shareappsettingswithgiraffe.models.shareHistory.History
import com.example.shareappsettingswithgiraffe.models.shareHistory.ShareHistory
import com.example.shareappsettingswithgiraffe.network.ApiRepository
//import com.example.shareappsettingswithgiraffe.data.database.ShareHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel

class MainViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val dataBaseRepository: DataBaseRepository,
) : ViewModel() {


    //    var _mutableStateOfList = mutableStateListOf<SecIDLastOpen>()
    //    val mutableStateOfList : List<SecIDLastOpen> = _mutableStateOfList
    //
    //    val _simpleMutableListForLastAndOpen: MutableList<SecIDLastOpen> = mutableListOf()
    //    val simpleListForLastAndOpen: List<SecIDLastOpen> = _simpleMutableListForLastAndOpen
    //
    //    private val _allCurentLastAndOpen = MutableLiveData<MutableList<SecIDLastOpen>>()
    //    val allCurentLastAndOpen: LiveData<MutableList<SecIDLastOpen>>
    //        get() = _allCurentLastAndOpen


    val loadingFlow = flow<String> {
        while (true) {
            emit("Идет загрузка акций.")
            delay(1000L)
            emit("Идет загрузка акций..")
            delay(1000L)
            emit("Идет загрузка акций...")
            delay(1000L)
        }

    }

    private val _downloadingState = MutableStateFlow<DataUpdateUIState>(DataUpdateUIState.IsLoading)
    val downLoadingState = _downloadingState.asStateFlow()


    private val _stageOfDownloading = MutableStateFlow<StagesOfUpdate>(StagesOfUpdate(message = ""))
    val stageOfDownloading = _stageOfDownloading.asStateFlow()


    // MutableStateOfValue
    var historyVal by mutableStateOf<ShareHistory>(ShareHistory())
        private set

    private val _showMainTopBar = MutableStateFlow(true)
    val showMainTopBar = _showMainTopBar.asStateFlow()

    private val _showMainBottomBar = MutableStateFlow(true)
    val showMainBottomBar = _showMainBottomBar.asStateFlow()


    suspend fun UploadHistoryAndShares() {
        setLoadingState()
        UpdateTheHistoryUltimate()
        setCompletedState()
    }


    suspend fun deleteAllSharesHistory() {
        dataBaseRepository.deleteAllSharesHistory()
    }

    suspend fun deleteFirstLoad() {
        dataBaseRepository.deleteFirstLoad()
    }


    suspend fun CheckIfFirstLoad(): Boolean {
        val result: FirstLoad? = dataBaseRepository.getFirstUploadById(isFirstCreatedId = 1)
        return result == null
    }

    fun showMainTopBarFun(parameter: Boolean) {
        _showMainTopBar.value = parameter
    }

    fun showMainBottomBarFun(parameter: Boolean) {
        _showMainBottomBar.value = parameter
    }

    fun setLoadingState() {
        _downloadingState.value = DataUpdateUIState.IsLoading
    }

    fun setCompletedState() {
        _downloadingState.value = DataUpdateUIState.LoadingHasCompleted
    }


    suspend fun UpdateTheHistoryUltimate() {
        viewModelScope.launch(Dispatchers.IO) {
            UpdateSharesAndItsHistoryForFewShares()
            //  SetStageOfDownloading(newValue = "Загрузка завершена")
            ChangeFirstLoad()
        }.join()
    }

    suspend fun ChangeFirstLoad() {

        val currentDay = LocalDate.now()
        val currentDayString = currentDay.toString()

        val firstLoadEntity = FirstLoad(
            isFirstCreatedId = 1,
            isFirstCreated = 0,
            dayOfDBUpdate = currentDayString
        )

        dataBaseRepository.insertFirstLoad(
            firstLoadEntity
        )
    }


    // Uploads secIds, short names and history for all shares from indexMB
    suspend fun UpdateSharesAndItsHistoryForFewShares() {

        //  SetStageOfDownloading(newValue = "Загружаем список акций Индекса Мосбиржи")

        val listOfFavoriteShares = viewModelScope.async {
            val fiteredList = dataBaseRepository.getAllSharesWithoutFlow().filter {
                it.isFavorite
            }
            fiteredList
        }.await()

        dataBaseRepository.deleteAllHistory()
        dataBaseRepository.deleteAllShares()

        val TAG = "SavingHistoryInDB:"
        viewModelScope.launch(Dispatchers.IO) {
            // Take all secIds from IndexMb
            val time = measureTimeMillis {
                //uploads secIds, short names for all shares from indexMB


                val sharesList =
                    async { getSharesFromMbwithSecIdAndShortName(listOfPreviouslyFavoriteShares = listOfFavoriteShares) }
                println("$TAG sharesList is ${sharesList.await()}")
                println("$TAG The size of sharesList is ${sharesList.await().size}")

                val dateRangeAsync = async { getAllDatesFromNow() }


                //  sharesList.await()
                //   SetStageOfDownloading(newValue = "Список Акций Индекса Мосбиржи загружен")


                /**The list of shares has been uploaded*/

//                SetStageOfDownloading(newValue = "Получаем историю котировок")


//                val deferredResults = sharesList.await().take(2).map { SecIdAndShortName ->
                val deferredResults = sharesList.await().map { SecIdAndShortName ->
                    async(Dispatchers.IO) {
                        getHistoryForSpecificShare(
                            secId = SecIdAndShortName[0],
                            listOfControlPoints = dateRangeAsync.await()
                        )
                    }
                }
                deferredResults.awaitAll()
            }
            Log.d(TAG, "TIME TO GET ALL HISTORY FOR ALL SECIDS TOOK ${time / 1000f} seconds.")
        }.join()


    }


    suspend fun getHistoryForSpecificShare(secId: String, listOfControlPoints: List<LocalDate>) {
        val TAG = "SavingHistoryInDB"
        val Tag = "MainViewModel:"

        val deferredResult = mutableListOf<Deferred<List<History>>>()
        for (i in 0 until listOfControlPoints.size - 1) {

            val deferred = viewModelScope.async(Dispatchers.IO) {
                getHistoryForSecWithDataRange(
                    secId = secId,
                    dateRangeStart = listOfControlPoints[i].toString(),
                    dateRangeEnd = listOfControlPoints[i + 1].toString()
                )
            }
            deferredResult.add(deferred)
        }

        val answers = deferredResult.awaitAll().flatten()
        // SetStageOfDownloading(newValue = "Загружаем котировки акций для $secId")
        //  println("HistoryUploding: For ${SecIdAndShortName[0]} history fetching is started.......")
        println("$TAG: the size of history for $secId is :${answers.size} ")


        /**HERE I SAVE THE HISTORY IN THE DATABASE*/

        val booleanForWritingInDB = viewModelScope.launch {

            //   SetStageOfDownloading(newValue = "Загружаем котировки $secId в базу данных")
            val time = measureTimeMillis {

                answers.map {
                    val shareHistoryItem: com.example.shareappsettingswithgiraffe.data.database.ShareHistory =
                        com.example.shareappsettingswithgiraffe.data.database.ShareHistory(
                            historyDate = it.TRADEDATE,
                            historyClosePrice = it.CLOSE,
                            secId = secId,
                        )
                    dataBaseRepository.insertHistoryItem(shareHistoryItem)
                }
            }

            println("$TAG: Time to write In the DB For $secId is :${time / 1000f} seconds ")
        }.join()

        println("$TAG: For $secId we are OUTSIDE OF viewModelScope.launch for writing in the DB")
    }


    // Uploads every shares from IndexMB with SecId and short name
    suspend fun getSharesFromMbwithSecIdAndShortName(
        listOfPreviouslyFavoriteShares: List<ShareEntity> = emptyList(),
    ): List<List<String>> {
        val TAG = "TheMainFunction L0"
        var temporaryList: List<List<String>> = emptyList()
        val listOfSharesFromIndexMB = viewModelScope.async(Dispatchers.IO) {
            repository.getTheListOfSharesOfMBWithSecIdAndShortName().let { Response ->
                if (Response.isSuccessful) {

                    Response.body()?.analytics?.data?.let { data ->
                        println("TheMainFunction L0: the data is : $data")

                        launch {
                            data.forEach { shareInfo ->
                                launch {
                                    val existingShare: ShareEntity? =
                                        dataBaseRepository.getShareBySecId(shareInfo[0])

                                    var isFavorite = false
                                    val shareSecId = shareInfo[0]
                                    val shareShortName = shareInfo[1]
                                    val ifThereIsTheElement = listOfPreviouslyFavoriteShares.any {
                                        it.secId == shareSecId
                                    } // returns boolean of there is an element that fulfils the condition

                                    if (ifThereIsTheElement) {
                                        val share = ShareEntity(
                                            secId = shareInfo[0],
                                            shortName = shareShortName,
                                            isFavorite = true
                                        )
                                        dataBaseRepository.insertShare(share)
                                    } else {
                                        val share = ShareEntity(
                                            secId = shareInfo[0],
                                            shortName = shareShortName,
                                            isFavorite = false
                                        )
                                        dataBaseRepository.insertShare(share)
                                    }
                                }
                            }
                        }
                        launch { temporaryList = data }
                    }
                } else {
                    Log.d(TAG, "Failed to load history: ${Response.errorBody()}")
                }
            }
        }
        listOfSharesFromIndexMB.await()
        return temporaryList
    }


    fun getAllDatesFromNow(): ArrayList<LocalDate> {
        val currentDate = LocalDate.now()
        val fiveYearsAgo = currentDate.minusYears(5)

        val datesForHistoryCall = ArrayList<LocalDate>()
        datesForHistoryCall.add(fiveYearsAgo)
        var endPeriod: LocalDate
        for (i in 0..20) {
            endPeriod = datesForHistoryCall.last().plusMonths(3)
            datesForHistoryCall.add(endPeriod)
        }
        if (datesForHistoryCall.last() > currentDate) {
            datesForHistoryCall.removeLast()
            datesForHistoryCall.add(currentDate)
        }
        return datesForHistoryCall
    }

    suspend fun getHistoryForSecWithDataRange(
        secId: String,
        dateRangeStart: String,
        dateRangeEnd: String
    ): List<History> {
        val temporaryMutableList = mutableListOf<History>()
        val result = viewModelScope.async(Dispatchers.IO) {
            repository.getHistoryWithSecIdAndDataRange(
                secId = secId,
                dateRangeStart = dateRangeStart,
                dateRangeEnd = dateRangeEnd
            ).let { Response ->
                if (Response.isSuccessful) {
                    println("TagTest: the response is ${Response.body()} ")
                    println("TagTest: the response is ${Response.body()?.getOrNull(1)?.history} ")
                    val historyValue = Response.body()!!
                    val listOfHistory = historyValue.getOrNull(1)?.history.orEmpty()
//                        historyVal = Response.body()!!
//                        val listOfHistory = historyVal.getOrNull(1)?.history.orEmpty()
                    temporaryMutableList.addAll(listOfHistory)
                }

            }
        }
        result.await()
        return temporaryMutableList.toList()
    }

}