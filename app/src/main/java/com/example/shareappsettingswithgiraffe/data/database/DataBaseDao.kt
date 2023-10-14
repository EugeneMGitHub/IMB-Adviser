package com.example.shareappsettingswithgiraffe.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import com.example.shareappsettingswithgiraffe.models.shareHistory.History
import kotlinx.coroutines.flow.Flow

@Dao
interface DataBaseDao {


    /**For ShareEntity*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShare(shareEntity: ShareEntity)

    @Update
    suspend fun updateShare(share: ShareEntity)

    @Query("SELECT * FROM ShareEntity WHERE secId =:secId")
    suspend fun getShareBySecId(secId: String) : ShareEntity

    @Query("SELECT * FROM ShareEntity")
    fun getAllShares(): Flow<List<ShareEntity>>

    @Query("SELECT * FROM ShareEntity")
    suspend fun getAllSharesWithoutFlow(): List<ShareEntity>


    @Query("SELECT * FROM ShareEntity WHERE isFavorite =:isFavorite")
    fun getAllSharesByIsFavorite(isFavorite: Boolean): Flow<List<ShareEntity>>

    @Query("SELECT * FROM ShareEntity WHERE isFavorite =:isFavorite")
    suspend fun getAllSharesByIsFavoriteWithoutFlow(isFavorite: Boolean): List<ShareEntity>




    /**For ShareHistory*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(historyItem: ShareHistory)

    @Query("DELETE FROM ShareHistory")
    suspend fun deleteAllHistory()

    @Query("DELETE FROM ShareEntity")
    suspend fun deleteAllShares()

    @Query("SELECT * FROM ShareHistory WHERE secId =:secId")
    fun getAllHistory(secId: String) : Flow<List<ShareHistory>>

    @Query("SELECT * FROM ShareHistory WHERE secId =:secId")
    suspend fun getAllHistoryWithoutFlow(secId: String) : List<ShareHistory>

    @Query("DELETE FROM ShareHistory")
    suspend fun deleteAllSharesHistory()




    /**For NewsUpdateStrategy*/

    @Query("DELETE FROM NewsUpdateStrategy")
    suspend fun deleteSearchStrategy()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchStrategy(SearchStrategy : NewsUpdateStrategy)

    @Query("SELECT * FROM NewsUpdateStrategy WHERE strategyId = :strategyId")
    suspend fun getSearchStrategyById(strategyId: Int) : NewsUpdateStrategy

    @Query("SELECT * FROM NewsUpdateStrategy")
    suspend fun getAllSearchStrategy() : List<NewsUpdateStrategy>

    @Update
    suspend fun updateSearchStrategy(newsUpdateStrategy: NewsUpdateStrategy)



    /**For FirstLoad*/

    @Query("SELECT * FROM FirstLoad WHERE isFirstCreatedId = :isFirstCreatedId")
    suspend fun getFirstUploadById(isFirstCreatedId: Int) : FirstLoad

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFirstLoad(firstLoadEntity: FirstLoad)

    @Query("DELETE FROM FirstLoad")
    suspend fun deleteFirstLoad()




    /**For ShareNews*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(shareNews : ShareNews)

    @Query("SELECT * FROM ShareNews")
    fun getAllNews() : Flow<List<ShareNews>>

    @Query("SELECT * FROM ShareNews")
    fun getAllNewsWithoutFlow() : List<ShareNews>

    @Query("DELETE FROM ShareNews")
    suspend fun deleteAllNews()

    @Delete
    suspend fun deleteNews(shareNews: ShareNews)



}