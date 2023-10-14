package com.example.shareappsettingswithgiraffe.data.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shareappsettingswithgiraffe.models.shareHistory.History
import kotlinx.coroutines.flow.Flow

interface DataBaseRepository {

    /**For ShareEntity*/
    suspend fun insertShare(shareEntity: ShareEntity)
    fun getAllShares(): Flow<List<ShareEntity>>
    suspend fun getAllSharesWithoutFlow(): List<ShareEntity>
    fun getAllSharesByIsFavorite(isFavorite: Boolean): Flow<List<ShareEntity>>
    suspend fun getAllSharesByIsFavoriteWithoutFlow(isFavorite: Boolean): List<ShareEntity>
    suspend fun updateShare(share: ShareEntity)
    suspend fun getShareBySecId(secId: String) : ShareEntity
    suspend fun deleteAllShares()


    /**For ShareHistory*/
    suspend fun insertHistoryItem(historyItem: ShareHistory)
    fun getAllHistory(secId: String) : Flow<List<ShareHistory>>
    suspend fun getAllHistoryWithoutFlow(secId: String) : List<ShareHistory>
    suspend fun deleteAllSharesHistory()
    suspend fun deleteAllHistory()


    /**For NewsUpdateStrategy*/
    suspend fun deleteSearchStrategy()
    suspend fun insertSearchStrategy(SearchStrategy : NewsUpdateStrategy)
    suspend fun updateSearchStrategy(newsUpdateStrategy: NewsUpdateStrategy)
    suspend fun getSearchStrategyById(strategyId: Int) : NewsUpdateStrategy
    suspend fun getAllSearchStrategy() : List<NewsUpdateStrategy>


    /**For FirstLoad*/
    suspend fun getFirstUploadById(isFirstCreatedId: Int) : FirstLoad
    suspend fun insertFirstLoad(firstLoadEntity: FirstLoad)
    suspend fun deleteFirstLoad()


    /**For ShareNews*/
    suspend fun insertNews(shareNews : ShareNews)
    fun getAllNews() : Flow<List<ShareNews>>
    suspend fun deleteAllNews()
    suspend fun deleteNews(shareNews: ShareNews)
    fun getAllNewsWithoutFlow() : List<ShareNews>



















}