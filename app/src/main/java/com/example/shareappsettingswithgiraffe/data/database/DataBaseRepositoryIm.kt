package com.example.shareappsettingswithgiraffe.data.database

import com.example.shareappsettingswithgiraffe.models.shareHistory.History
import kotlinx.coroutines.flow.Flow

class DataBaseRepositoryIm(
    private val dataBaseDao: DataBaseDao
) : DataBaseRepository {


    /**For ShareEntity*/

    override suspend fun insertShare(shareEntity: ShareEntity) {
        dataBaseDao.insertShare(shareEntity)
    }

    override fun getAllShares(): Flow<List<ShareEntity>> {
        return dataBaseDao.getAllShares()
    }

    override suspend fun getAllSharesWithoutFlow(): List<ShareEntity> {
        return dataBaseDao.getAllSharesWithoutFlow()
    }

    override fun getAllSharesByIsFavorite(isFavorite: Boolean): Flow<List<ShareEntity>> {
        return dataBaseDao.getAllSharesByIsFavorite(isFavorite)
    }

    override suspend fun getAllSharesByIsFavoriteWithoutFlow(isFavorite: Boolean): List<ShareEntity> {
        return dataBaseDao.getAllSharesByIsFavoriteWithoutFlow(isFavorite)
    }

    override suspend fun deleteAllShares() {
        dataBaseDao.deleteAllShares()
    }

    override suspend fun getShareBySecId(secId: String): ShareEntity {
        return dataBaseDao.getShareBySecId(secId)
    }

    override suspend fun updateShare(share: ShareEntity) {
        dataBaseDao.updateShare(share)
    }


    /**For ShareHistory*/

    override suspend fun insertHistoryItem(historyItem: ShareHistory) {
        dataBaseDao.insertHistoryItem(historyItem)
    }

    override suspend fun deleteAllSharesHistory() {
        dataBaseDao.deleteAllSharesHistory()
    }

    override fun getAllHistory(secId: String): Flow<List<ShareHistory>> {
        return dataBaseDao.getAllHistory(secId)
    }

    override suspend fun deleteAllHistory() {
        dataBaseDao.deleteAllHistory()
    }

    override suspend fun getAllHistoryWithoutFlow(secId: String): List<ShareHistory> {
        return dataBaseDao.getAllHistoryWithoutFlow(secId)
    }


    /**For NewsUpdateStrategy*/

    override suspend fun deleteSearchStrategy() {
        dataBaseDao.deleteSearchStrategy()
    }

    override suspend fun updateSearchStrategy(newsUpdateStrategy: NewsUpdateStrategy) {
        dataBaseDao.updateSearchStrategy(newsUpdateStrategy)
    }

    override suspend fun insertSearchStrategy(SearchStrategy: NewsUpdateStrategy) {
        return dataBaseDao.insertSearchStrategy(SearchStrategy)
    }

    override suspend fun getAllSearchStrategy(): List<NewsUpdateStrategy> {
        return dataBaseDao.getAllSearchStrategy()
    }

    override suspend fun getSearchStrategyById(strategyId: Int): NewsUpdateStrategy {
        return dataBaseDao.getSearchStrategyById(strategyId)
    }


    /**For FirstLoad*/
    override suspend fun getFirstUploadById(isFirstCreatedId: Int): FirstLoad {
        return dataBaseDao.getFirstUploadById(isFirstCreatedId)
    }

    override suspend fun insertFirstLoad(firstLoadEntity: FirstLoad) {
        dataBaseDao.insertFirstLoad(firstLoadEntity)
    }

    override suspend fun deleteFirstLoad() {
        dataBaseDao.deleteFirstLoad()
    }


    /**For ShareNews*/
    override fun getAllNewsWithoutFlow(): List<ShareNews> {
        return dataBaseDao.getAllNewsWithoutFlow()
    }

    override suspend fun insertNews(shareNews: ShareNews) {
        dataBaseDao.insertNews(shareNews)
    }

    override suspend fun deleteNews(shareNews: ShareNews) {
        dataBaseDao.deleteNews(shareNews)
    }

    override suspend fun deleteAllNews() {
        dataBaseDao.deleteAllNews()
    }

    override fun getAllNews(): Flow<List<ShareNews>> {
        return dataBaseDao.getAllNews()
    }


}