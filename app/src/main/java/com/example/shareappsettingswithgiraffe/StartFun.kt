package com.example.shareappsettingswithgiraffe

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.shareappsettingswithgiraffe.navigation.RoutesBottomNavigation
import com.example.shareappsettingswithgiraffe.navigation.SetupNavHost
import com.example.shareappsettingswithgiraffe.navigation.mainBottomBarTabs
import com.example.shareappsettingswithgiraffe.ui.screens.infoScreen.infoScreenViewModel
import com.example.shareappsettingswithgiraffe.ui.screens.mainScreen.mainScreenViewModel
import com.example.shareappsettingswithgiraffe.ui.screens.newsScreen.NewsScreenViewModel
import com.example.shareappsettingswithgiraffe.ui.screens.sharesScreen.SharesScreenViewModel
import com.example.shareappsettingswithgiraffe.ui.theme.bluish
import com.example.shareappsettingswithgiraffe.workers.CustomWorker
import com.example.shareappsettingswithgiraffe.workers.CustomWorker.Companion.Progress
import java.time.Duration
import java.util.concurrent.TimeUnit


//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@SuppressLint("SuspiciousIndentation")
// used for BadgeBox to work
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartFun(applicationContextVal: Context) {


    val mainViewModel: MainViewModel = hiltViewModel()
    val NewsScreenViewModel: NewsScreenViewModel = hiltViewModel()
    val ShareScreenViewModel: SharesScreenViewModel = hiltViewModel()
    val mainScreenViewModel: mainScreenViewModel = hiltViewModel()
    val infoScreenViewModel: infoScreenViewModel = hiltViewModel()


    LaunchedEffect(key1 = Unit) {


//        mainViewModel.deleteFirstLoad()
//        mainViewModel.deleteAllSharesHistory()


        // Checking Out if the app was started for the first time
        val ifFirstLoad =
            mainViewModel.CheckIfFirstLoad() // returns true if the app was started for the first time

        println("SavingHistoryInDB:   ifFirstLoad is $ifFirstLoad")

        if (ifFirstLoad) {
            NewsScreenViewModel.DeleteAllTheNews()

            println("SavingHistoryInDB:   ifFirstLoad is empty")
            mainScreenViewModel.insertDefaultStrategy()
            mainScreenViewModel.LoadConditionsForStrategyFromDB()
            println("SavingHistoryInDB:   Now we are about to update the History and Shares")
            mainViewModel.UploadHistoryAndShares()
            println("SavingHistoryInDB:   Updating is Complete")
            println("SavingHistoryInDB:   Have set  CompletetedState")
            println("SavingHistoryInDB:   Now calculating the news")
            // creates a list of news in the NewsScreen
            NewsScreenViewModel.CalculateTheNews()
            println("SavingHistoryInDB:   Stopped calculating the news")
        } else {
            // in case the app wasn't started for the first time
            mainViewModel.setCompletedState()
            println("SavingHistoryInDB:   ifFirstLoad is NOT empty")
        }
    }


    val lifecycleOwner = LocalLifecycleOwner.current
    // launching a repeatable worker to update the history DB and Shares everyday from within 7:00-10:00
    LaunchedEffect(key1 = Unit) {

        val workerConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<CustomWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.HOURS,
        )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                duration = Duration.ofMinutes(15)
            )
            .setConstraints(workerConstraints)
            .build()

        val workManager = WorkManager.getInstance(applicationContextVal)
        workManager.enqueueUniquePeriodicWork(
            "Reuploding_history_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        // allows to observe changes in the state of the worker. More useful for one time worker
        workManager.getWorkInfosForUniqueWorkLiveData("Reuploding_history_worker")
            .observe(lifecycleOwner) { workInfoList ->
                workInfoList.forEach { workInfo ->
                    Log.d("Reuploding_history_worker", "${workInfo.state}")
                    // if we want to get a progress val from the worker. Works almost as outputData
                    val progress = workInfo.progress
                    val value = progress.getInt(Progress, 0) // Access the progress data
                    Log.d("Reuploding_history_worker", "Progress is $value")
                }
            }

        // In case we want to cancel the worker
        // workManager.cancelUniqueWork("Reuploding_history_worker")
    }


    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    val topAppBarValue = mainViewModel.showMainTopBar.collectAsState(initial = true)
    val topBottomBarValue = mainViewModel.showMainBottomBar.collectAsState(initial = true)

    // rememberSaveable helps survive screen rotation etc.
    var bottomNavState by rememberSaveable {
        mutableStateOf(1)
    }
    var bottomNavStateRoutes by rememberSaveable {
        mutableStateOf(RoutesBottomNavigation.Main.route)
    }

    // The number of news in the NewsScreen for its icon's badge
    val newsCountForBadge =
        NewsScreenViewModel.listOfNews1.collectAsState(initial = emptyList()).value.size

    // in Material 3 we use snackbarHostState instead of ScaffoldState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        // Just saved here the way to implemet a topBar
        //topBar = {
        //        val currentScreen = mainTopBarTabs.find { it.route == currentDestination?.route } ?: Routes.Main
        //        if (topAppBarValue.value == true) {
        //            MainTopBar(
        //                topBarScreens = mainTopBarTabs,
        //                currentScreen = currentScreen,
        //                onClick = { navController.navigate(it.route) }
        //            )
        //        }
        //     },

        bottomBar = {
            val currentScreenWithBottomBar =
                mainBottomBarTabs.find { it.route == currentDestination?.route }
                    ?: RoutesBottomNavigation.Main
            if (topBottomBarValue.value) {

                NavigationBar(
                    containerColor = Color(0xFFA8A8A8)
                ) {
                    mainBottomBarTabs.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = currentScreenWithBottomBar.route == item.route,
                            onClick = {
                                bottomNavState = index
                                bottomNavStateRoutes = item.route
                                navController.navigate(item.route)
                            },
                            icon = {
                                BadgedBox(badge = {
                                    if (item.hasBadge) Badge {}
                                    if (item.route == RoutesBottomNavigation.News.route) {
                                        Badge(
                                            //  containerColor = bluish
                                            containerColor = MaterialTheme.colorScheme.primary
                                        ) {
                                            Text(text = "$newsCountForBadge")
                                        }
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (bottomNavState == index) item.selectedIcon
                                        else item.unSelectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            },
                            label = { Text(text = item.title) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.background,
                                selectedTextColor = MaterialTheme.colorScheme.background,
                                indicatorColor = Color(0xFFA8A8A8),
                                unselectedIconColor = Color(0xFF808080),
                                unselectedTextColor = Color(0xFF808080),
                            )
                        )
                    }

                }


            }


        }

    ) {
        val scaffoldBottomPadding = it.calculateBottomPadding()

        SetupNavHost(
            scaffoldBottomPadding = scaffoldBottomPadding,
            navController = navController,
            mainViewModel = mainViewModel,
            mainScreenViewModel = mainScreenViewModel,
            infoScreenViewModel = infoScreenViewModel,
            ShareScreenViewModel = ShareScreenViewModel,
            NewsScreenViewModel = NewsScreenViewModel,
            snackbarHostState = snackbarHostState
        )
    }


}