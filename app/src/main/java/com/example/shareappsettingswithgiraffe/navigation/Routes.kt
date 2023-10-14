package com.example.shareappsettingswithgiraffe.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.shareappsettingswithgiraffe.utils.Constants

sealed class Routes(val icon: ImageVector, val route: String) {
    object Splash : Routes(icon = Icons.Filled.Home, route = Constants.Screens.SPLASH_SCREEN)
    object Main : Routes(icon = Icons.Filled.Home, route = Constants.Screens.MAIN_SCREEN)
    object Shares : Routes(icon = Icons.Filled.Menu, route = Constants.Screens.SHARES_SCREEN)
    object News : Routes(icon = Icons.Filled.Notifications, route = Constants.Screens.NEWS_SCREEN)
    object InfoScreen : Routes(icon = Icons.Filled.Home, route = Constants.Screens.INFO_SCREEN)
}

val mainTopBarTabs = listOf(
    Routes.Shares,
    Routes.Main,
    Routes.News
)


sealed class RoutesBottomNavigation(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val hasBadge: Boolean,
    val message: Int,
    val route: String,
) {
    object Splash : RoutesBottomNavigation(
        title = Constants.Screens.SPLASH_SCREEN,
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home,
        hasBadge = false,
        message = 0,
        route = Constants.Screens.SPLASH_SCREEN
    )


    object Main : RoutesBottomNavigation(
        title = Constants.Screens.MAIN_SCREEN,
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home,
        hasBadge = false,
        message = 0,
        route = Constants.Screens.MAIN_SCREEN
    )


    object Shares : RoutesBottomNavigation(
        title = Constants.Screens.SHARES_SCREEN,
        selectedIcon = Icons.Filled.Menu,
        unSelectedIcon = Icons.Outlined.Menu,
        hasBadge = false,
        message = 0,
        route = Constants.Screens.SHARES_SCREEN
    )


    object News : RoutesBottomNavigation(
        title = Constants.Screens.NEWS_SCREEN,
        selectedIcon = Icons.Filled.Notifications,
        unSelectedIcon = Icons.Outlined.Notifications,
        hasBadge = true,
        message = 0,
        route = Constants.Screens.NEWS_SCREEN
    )

    object InfoScreen : RoutesBottomNavigation(
        title = Constants.Screens.INFO_SCREEN,
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home,
        hasBadge = false,
        message = 0,
        route = Constants.Screens.INFO_SCREEN
    )
}

val mainBottomBarTabs = listOf<RoutesBottomNavigation>(
    RoutesBottomNavigation.Shares,
    RoutesBottomNavigation.Main,
    RoutesBottomNavigation.News,
)

