package com.example.shareappsettingswithgiraffe.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shareappsettingswithgiraffe.MainViewModel
import com.example.shareappsettingswithgiraffe.ui.screens.infoScreen.InfoScreen
import com.example.shareappsettingswithgiraffe.ui.screens.infoScreen.infoScreenViewModel
import com.example.shareappsettingswithgiraffe.ui.screens.mainScreen.MainScreen
import com.example.shareappsettingswithgiraffe.ui.screens.mainScreen.mainScreenViewModel
import com.example.shareappsettingswithgiraffe.ui.screens.newsScreen.NewsScreen
import com.example.shareappsettingswithgiraffe.ui.screens.newsScreen.NewsScreenViewModel
import com.example.shareappsettingswithgiraffe.ui.screens.sharesScreen.SharesScreen
import com.example.shareappsettingswithgiraffe.ui.screens.sharesScreen.SharesScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun SetupNavHost(
    scaffoldBottomPadding: Dp,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    mainViewModel: MainViewModel,
    mainScreenViewModel: mainScreenViewModel,
    infoScreenViewModel: infoScreenViewModel,
    NewsScreenViewModel: NewsScreenViewModel,
    ShareScreenViewModel: SharesScreenViewModel,
) {


    val SetupNavHostCoroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Routes.Main.route

    ) {


        composable(route = Routes.Main.route) {
            MainScreen(
                mainScreenViewModel = mainScreenViewModel,
                mainViewModel = mainViewModel,
                snackbarHostState = snackbarHostState,
                onUpdateNewsBtnClickedOn = {
                    SetupNavHostCoroutineScope.launch {
                        NewsScreenViewModel.CalculateTheNews()
                    }

                }
            )
        }


        composable(route = Routes.Shares.route) {
            SharesScreen(
                mainViewModel = mainViewModel,
                scaffoldBottomPadding = scaffoldBottomPadding,
                onShareClick = {
                    navController.navigate(Routes.InfoScreen.route + "/" + it)
                    mainViewModel.showMainTopBarFun(false)
                    mainViewModel.showMainBottomBarFun(false)
                },
                ShareScreenViewModel = ShareScreenViewModel,
                NewsScreenViewModel = NewsScreenViewModel,
                snackbarHostState = snackbarHostState,
            )
        }


        composable(
            route = Routes.News.route,
        ) {
            NewsScreen(
                mainViewModel = mainViewModel,
                scaffoldBottomPadding = scaffoldBottomPadding,
                NewsScreenViewModel = NewsScreenViewModel,
                onShareClick = {
                    navController.navigate(Routes.InfoScreen.route + "/" + it)
                    mainViewModel.showMainTopBarFun(false)
                    mainViewModel.showMainBottomBarFun(false)
                }
            )
        }


        composable(
            route = Routes.InfoScreen.route + "/{secId}",
            arguments = listOf(
                navArgument(name = "secId") {
                    type = NavType.StringType
                    defaultValue = "SBER"
                    nullable = true
                }
            )
        ) { entry ->
            val secId = entry.arguments?.getString("secId")

            InfoScreen(
                infoScreenViewModel = infoScreenViewModel,
                secId = secId,
                onPopBackStack = {
                    navController.popBackStack()
                    mainViewModel.showMainTopBarFun(true)
                    mainViewModel.showMainBottomBarFun(true)
                },
                onHeartClick = { secId, isLiked ->
                    ShareScreenViewModel.changeShareIsFavoriteStatus(secId, isLiked)
                }
            )
        }
    }
}