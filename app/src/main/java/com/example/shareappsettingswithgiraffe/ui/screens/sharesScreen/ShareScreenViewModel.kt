package com.example.shareappsettingswithgiraffe.ui.screens.sharesScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shareappsettingswithgiraffe.data.database.DataBaseRepository
import com.example.shareappsettingswithgiraffe.data.database.ShareEntity
import com.example.shareappsettingswithgiraffe.models.shareHistory.History
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharesScreenViewModel @Inject constructor(
    private val dataBaseRepository: DataBaseRepository,
) : ViewModel() {

    //    private val _liveDataTodos = MutableLiveData<List<ShareEntity>>()
    //    val liveDataTodos: LiveData<List<ShareEntity>
    //    get() = _liveDataTodos

    //    private val _uiState = MutableStateFlow(UiStateClass())
    //    val uiState: StateFlow<UiStateClass> = _uiState.asStateFlow()

    // StateFlow
    // private val _stateFlowShares = MutableStateFlow<List<ShareEntity>>(emptyList())
    // val stateFlowShares = _stateFlowShares.asStateFlow()

    val allShares = dataBaseRepository.getAllShares()

    var theLastUpdateDate by mutableStateOf("")
        private set

    suspend fun getTheLastUpdateDate() {
        val date = dataBaseRepository.getFirstUploadById(1)
        println("getFirstUploadById: the date is: $date")

            theLastUpdateDate = date?.dayOfDBUpdate ?: ""

    }


    fun changeShareIsFavoriteStatus(secId: String, isFavorite: Boolean) {

        println("changeShareIsFavoriteStatus: Share with secId=$secId and isFavorite = $isFavorite is pressed")
        viewModelScope.launch {

            val existingShare = dataBaseRepository.getShareBySecId(secId)
            println("changeShareIsFavoriteStatus: existingShare is $existingShare")

            existingShare.let {
                val updatedShare = it.copy(isFavorite = isFavorite)
                dataBaseRepository.updateShare(updatedShare)
            }
        }
    }

}