package com.example.shareappsettingswithgiraffe.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shareappsettingswithgiraffe.MainRepository
import com.example.shareappsettingswithgiraffe.MainViewModel
import com.example.shareappsettingswithgiraffe.data.database.DataBaseRepository
import com.example.shareappsettingswithgiraffe.network.ApiRepository
import com.example.shareappsettingswithgiraffe.ui.screens.newsScreen.NewsScreenViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@HiltWorker
class CustomWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val mainRepository: MainRepository,
    private val repository: ApiRepository,
    private val dataBaseRepository: DataBaseRepository,
) : CoroutineWorker(context, workerParameters) {


    val CustomWorkerTAG = "MyWorkerMessages"
    private val mainViewModel: MainViewModel =
        MainViewModel(dataBaseRepository = dataBaseRepository, repository = repository)
    private val newsScreenViewModel: NewsScreenViewModel =
        NewsScreenViewModel(dataBaseRepository = dataBaseRepository, repository = repository)


    companion object {
        const val Progress = "Progress"
        private const val delayDuration = 2L
    }

    override suspend fun doWork(): Result {

        val ifFirstLoad = mainViewModel.CheckIfFirstLoad()
        Log.d(CustomWorkerTAG, "ifFirstLoad is $ifFirstLoad")


        /**When observing progress with LiveData outside of the worker*/
//        val firstUpdate = workDataOf(Progress to 1)
//        val lastUpdate = workDataOf(Progress to 0)
//        setProgress(firstUpdate)
//        setProgress(lastUpdate)


        val currentDay = LocalDate.now()
        val currentDayString = currentDay.toString()

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val currentTimeWithoutFormatter = LocalDateTime.now().toLocalTime()
        val currentTime = LocalDateTime.now().format(formatter)

        val sevenOclockString = "07:00"
        val SevenOclock = LocalTime.parse(sevenOclockString, formatter)

        val tenOclockString = "10:00"
        val tenOclock = LocalTime.parse(tenOclockString, formatter)

        Log.d(CustomWorkerTAG, "currentTime is $currentTime")
        Log.d(CustomWorkerTAG, "SevenOclock is $SevenOclock")
        Log.d(CustomWorkerTAG, "tenOclock is $tenOclock")

        val currentTimeIsBiggerThanSeven: Boolean = currentTimeWithoutFormatter.isAfter(SevenOclock)
        val currentTimeIsLessThanTen: Boolean = currentTimeWithoutFormatter.isBefore(tenOclock)

//        if (currentTimeIsBiggerThanSeven && currentTimeIsLessThanTen && !ifFirstLoad) {
        if (currentTimeIsBiggerThanSeven && !ifFirstLoad) {
            Log.d(CustomWorkerTAG, "We can start uploading")
            var currentDayFromDB = dataBaseRepository.getFirstUploadById(1).dayOfDBUpdate
            Log.d(CustomWorkerTAG, "currentDayFromDB is $currentDayFromDB")
            Log.d(CustomWorkerTAG, "currentDay is $currentDayFromDB")

            if (currentDayFromDB == currentDayString) {
                Log.d(CustomWorkerTAG, "currentDay is equal to currentDayFromDB")
            } else {
                Log.d(
                    CustomWorkerTAG,
                    "currentDay is not equal to currentDayFromDB. Updating the history..."
                )

                withContext(Dispatchers.IO) {
//                val coroutineScope = CoroutineScope(coroutineContext)
                    launch {

                        mainViewModel.UploadHistoryAndShares()
                        Log.d(CustomWorkerTAG, "Now we are calculating the NEWS")
                        newsScreenViewModel.CalculateTheNews()
                        Log.d(CustomWorkerTAG, "Inside launch after updating...")
                    }.join()
                    Log.d(CustomWorkerTAG, "The database has been updated")
                }

                Log.d(CustomWorkerTAG, "This message is outside the withContext")

            }


        } else {
            Log.d(CustomWorkerTAG, "We can't start uploading. The conditions are not met")
        }



        return Result.success()
    }
}


