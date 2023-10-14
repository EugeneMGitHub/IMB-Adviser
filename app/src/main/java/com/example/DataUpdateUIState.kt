package com.example

sealed class DataUpdateUIState {

    object IsLoading : DataUpdateUIState()
    object LoadingHasCompleted : DataUpdateUIState()

//    sealed class Loading: DataUpdateUIState(){
//        object LoadFromDB : Loading()
//        object LoadFromNetwork : Loading()
//    }
//
//    data class Success(val message: String): DataUpdateUIState()
//    data class Error(val message: String): DataUpdateUIState()


}

