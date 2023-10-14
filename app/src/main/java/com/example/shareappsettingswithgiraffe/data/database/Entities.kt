package com.example.shareappsettingswithgiraffe.data.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ShareEntity(
    @PrimaryKey(autoGenerate = false)
    val secId: String,
    val shortName: String,
    val fullName: String = "",
    val isFavorite: Boolean
)

@Entity
data class ShareHistory(
    @PrimaryKey(autoGenerate = true)
    val historyId: Long = 0,
    val historyDate: String,
    val historyClosePrice: Double,
    val secId: String,
)

@Entity
data class ShareNews(
    @PrimaryKey(autoGenerate = true)
    val newsId: Int = 0,
    val changeDirection: String,
    val changePercent: String,
    val changePeriod: String,
    val comparisonTarget: String,
    val secId: String,

    // adding this column at migration 2
    // at migration 3 changing the name of thew coulumn from ColumnName = "opnPrice" to ColumnName = "currentPrice"
    @ColumnInfo(name = "currentPrice", defaultValue = "0")
    val currentPrice: Double = 0.0
)


@Entity
data class NewsUpdateStrategy(
    @PrimaryKey(autoGenerate = true)
    val strategyId: Int = 0,
    val sharesType: String,    // "Все акции индекса МБ", "Только избранные акции"
    val changeDirection: String,
    val comparisonTarget: String, // max цена, min цена, начало периода
    val changePeriod: String,
    val changeThresholdPercent: Float
)

// adding this table at migration 4
@Entity
data class FirstLoad(
    @PrimaryKey(autoGenerate = false)
    val isFirstCreatedId: Int,

    @ColumnInfo("isFirstCreated", defaultValue = "0")
    val isFirstCreated: Int = 0,

    // adding this field at migration 5
    @ColumnInfo("dayOfDBUpdate", defaultValue = "2020-01-01")
    val dayOfDBUpdate: String = "2020-01-01",

    // adding this field at migration 6
    @ColumnInfo("isToUpdate", defaultValue = "0")
    val isToUpdate: Int = 0,
)
