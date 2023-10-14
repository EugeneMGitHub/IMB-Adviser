package com.example.shareappsettingswithgiraffe.ui.screens.newsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shareappsettingswithgiraffe.data.database.DataBaseRepository
import com.example.shareappsettingswithgiraffe.data.database.ShareEntity
import com.example.shareappsettingswithgiraffe.network.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsScreenViewModel1 @Inject constructor(
    private val dataBaseRepository: DataBaseRepository,
    private val repository: ApiRepository,
) : ViewModel() {


    /**With job cancelation*/
//   suspend fun downloadListOfSharesByIsFavorite(isFavorite: Boolean = true) {
//       var job : Job? = null
//        val asyncValue = viewModelScope.async {
//            var size = 0
//            var hasFinished = false
//           job =  viewModelScope.launch {
//
//                val shares = dataBaseRepository.getAllSharesByIsFavorite(isFavorite)
//                    .collect { it: List<ShareEntity> ->
//                        println("NewsScreenVM1: allShares is with isFavorite = $isFavorite are $it")
//                        println("NewsScreenVM1: the size of shares is ${it.size}")
//                        size = it.size
//                        hasFinished = true
////                      job?.cancel()
//                        this.coroutineContext.job.cancel()
//                    }
//
//            }
//
//            job?.join()
//
//            size
//            }
//
//            println("NewsScreenVM1: Now we are outside of the collect function. The list size is ${asyncValue.await()}")
//        }


    /***/
    suspend fun downloadListOfSharesByIsFavorite(isFavorite: Boolean = true) {
        val asyncValue = viewModelScope.async {
            var size = 0

            // .first takes first emission anc cancel the flow

            val shares = dataBaseRepository.getAllSharesByIsFavorite(isFavorite).first()
            println("NewsScreenVM1: allShares is with isFavorite = $isFavorite are $shares")
            println("NewsScreenVM1: the size of shares is ${shares.size}")
            size = shares.size
            size
        }
        println("NewsScreenVM1: Now we are outside of the collect function. The list size is ${asyncValue.await()}")
    }


}