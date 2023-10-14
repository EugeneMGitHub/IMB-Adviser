package com.example.shareappsettingswithgiraffe.utils

import androidx.compose.material.icons.Icons
import com.example.shareappsettingswithgiraffe.data.database.NewsUpdateStrategy

class Constants {
    object Screens{
        const val SPLASH_SCREEN = "splash_screen"
        const val MAIN_SCREEN = "Главная"
        const val SHARES_SCREEN = "Акции ИМБ"
        const val NEWS_SCREEN = "Новости"
        const val INFO_SCREEN  = "info_screen"
    }

}


//val SearchStrategy : NewsUpdateStrategy = NewsUpdateStrategy(
//    sharesType = "Все акции индекса МБ",
//    changeDirection = "На повышении",
//    comparisonTarget = "Max цена",
//    changePeriod = "1 год",
//    changeThresholdPercent = 10f
//)


class StrategyOptions{

    object SharesType {
        const val ALL_SHARES = "Все акции индекса МБ"
        const val ONLY_FAVORITE = "Только избранные акции"
    }

    object ChangeDirection {
        const val INCREASING = "На повышении"
        const val DECREASING = "На понижении"
    }

    object СomparisonTarget {
        const val MAX_PRICE = "Max цена"
        const val MIN_PRICE = "Min цена"
        const val START_OF_THE_PERIOD = "Начало периода"
    }

    object ChangePeriod {
        const val ONE_WEEK = "1 неделя"
        const val ONE_MONTH = "1 месяц"
        const val THREE_MONTH = "3 месяца"
        const val SIX_MONTH = "6 месяцев"
        const val ONE_YEAR = "1 год"
        const val THREE_YEARS = "3 года"
        const val FIVE_YEARS = "5 лет"
    }

    object ChangeThresholdPercent {
//        const val DEFAULT_PERCENT = 22.2f
        const val DEFAULT_PERCENT = 0.1f
    }

}

