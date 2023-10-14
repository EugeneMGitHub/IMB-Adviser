package com.example.shareappsettingswithgiraffe

import android.util.Log
import com.example.shareappsettingswithgiraffe.data.database.DataBaseRepository
import com.example.shareappsettingswithgiraffe.network.ApiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainRepository @Inject constructor(
//    private val repository: ApiRepository,
//    private val dataBaseRepository: DataBaseRepository,
) {

    // Field injection let us get the instances of  the ApiRepository and dataBaseRepository classes when injecting MainRepository itself into a worker
    @Inject
    lateinit var repository: ApiRepository

    @Inject
    lateinit var dataBaseRepository: DataBaseRepository


    fun createLogs() {
        Log.d(
            "ReUPLODING_HISTORY_AND_SHARES",
            " MainRepository MainRepository MainRepository createLogs: mainrepository called"
        )
    }

    suspend fun GetSomethingDone(passedCoroutineScope: CoroutineScope) {
        passedCoroutineScope.launch {
            println("ReUPLODING_HISTORY_AND_SHARES : GetSomethingDone GetSomethingDoneGetSomethingDone")
        }

    }

}