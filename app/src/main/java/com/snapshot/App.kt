package com.snapshot

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.snapshot.feature.component.bottomBar.BottomNavigationBar
import com.snapshot.feature.component.topbar.TopBar
import com.snapshot.feature.screen.album.navigation.ALBUM_ROUTE
import com.snapshot.feature.screen.album.navigation.albumScreen
import com.snapshot.feature.screen.album.navigation.navigateToAlbum
import com.snapshot.feature.screen.chooseFrame.navigation.chooseFrameScreen
import com.snapshot.feature.screen.chooseFrame.navigation.navigateToChooseFrame
import com.snapshot.feature.screen.choosePhoto.navigation.choosePhotoScreen
import com.snapshot.feature.screen.choosePhoto.navigation.navigateToChoosePhoto
import com.snapshot.feature.screen.filter.navigation.filterScreen
import com.snapshot.feature.screen.filter.navigation.navigateToFilter
import com.snapshot.feature.screen.photo.navigation.navigateToPhoto
import com.snapshot.feature.screen.photo.navigation.photoScreen
import com.snapshot.feature.screen.photo.viewModel.PhotoViewModel
import com.snapshot.feature.screen.setting.navigation.settingScreen
import com.snapshot.feature.screen.splash.navigation.splashScreen
import com.snapshot.res.modifier.AppTheme
import com.snapshot.res.modifier.ColorTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map



@Composable
fun App(navHostController: NavHostController = rememberNavController()) {
    var currentRoute by remember { mutableStateOf(ALBUM_ROUTE) }
    var showBottomNav by remember { mutableStateOf(true) }
    val systemUiController = rememberSystemUiController()
    val photoViewModel: PhotoViewModel = viewModel()

    SideEffect {
        systemUiController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    LaunchedEffect(navHostController) {
        navHostController.currentBackStackEntryFlow
            .map { it.destination.route ?: ALBUM_ROUTE }
            .distinctUntilChanged()
            .collect {
                currentRoute = it
                showBottomNav = currentRoute !in listOf("photo")
            }
    }

    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {
                    if (showBottomNav) {
                        TopBar()
                    }
                },
                bottomBar = {
                    if (showBottomNav) {
                        BottomNavigationBar(navHostController)
                    }
                },
                containerColor = ColorTheme.colors.bg
            ) { innerPadding ->
                NavHost(
                    navController = navHostController,
                    startDestination = ALBUM_ROUTE,
                    modifier = Modifier.padding(innerPadding),
                    enterTransition = { getEnterTransition(initialState, targetState) },
                    exitTransition = { getExitTransition(initialState, targetState) },
                    popEnterTransition = { getPopEnterTransition(initialState, targetState) },
                    popExitTransition = { getPopExitTransition(initialState, targetState) },
                ) {
                    splashScreen(
                        navigateToHome = navHostController::navigateToAlbum
                    )
                    settingScreen()
                    albumScreen()
                    chooseFrameScreen(navigateToPhoto = navHostController::navigateToPhoto)
                    photoScreen(
                        navigateToChoosePhoto = navHostController::navigateToChoosePhoto,
                        navigateToAlbum = navHostController::navigateToAlbum,
                        viewModel = photoViewModel
                    )
                    choosePhotoScreen(
                        viewModel = photoViewModel,
                        navigateToFilter = navHostController::navigateToFilter,
                        navigateToPhoto = navHostController::navigateToPhoto
                    )
                    filterScreen()
                }
            }
        }
    }
}


enum class TransitionDirection {
    LEFT, RIGHT, UP, DOWN
}

private val transitionMap = mapOf(
    "setting" to mapOf(
        "album" to TransitionDirection.LEFT,
    ),
    "album" to mapOf(
        "setting" to TransitionDirection.RIGHT,
    ),

    )

private fun getTransitionDirection(from: String?, to: String?): TransitionDirection? {
    return transitionMap[from]?.get(to)
}

fun getEnterTransition(initial: NavBackStackEntry, target: NavBackStackEntry): EnterTransition {
    val from = initial.destination.route
    val to = target.destination.route

    return when {
        from == "move" && to == "signMove" -> slideInVertically { it } + fadeIn()
        from == "signMove" && to == "move" -> slideInVertically { -it } + fadeIn()
        getTransitionDirection(
            from,
            to
        ) == TransitionDirection.LEFT -> slideInHorizontally { -it } + fadeIn()

        getTransitionDirection(
            from,
            to
        ) == TransitionDirection.RIGHT -> slideInHorizontally { it } + fadeIn()

        getTransitionDirection(
            from,
            to
        ) == TransitionDirection.UP -> slideInVertically { -it } + fadeIn()

        getTransitionDirection(
            from,
            to
        ) == TransitionDirection.DOWN -> slideInVertically { it } + fadeIn()

        else -> EnterTransition.None
    }
}

fun getExitTransition(initial: NavBackStackEntry, target: NavBackStackEntry): ExitTransition {
    val from = initial.destination.route
    val to = target.destination.route

    return when {
        from == "move" && to == "signMove" -> slideOutVertically { -it } + fadeOut()
        from == "signMove" && to == "move" -> slideOutVertically { it } + fadeOut()
        getTransitionDirection(
            from,
            to
        ) == TransitionDirection.LEFT -> slideOutHorizontally { it } + fadeOut()

        getTransitionDirection(
            from,
            to
        ) == TransitionDirection.RIGHT -> slideOutHorizontally { -it } + fadeOut()

        getTransitionDirection(
            from,
            to
        ) == TransitionDirection.UP -> slideOutVertically { -it } + fadeOut()

        getTransitionDirection(
            from,
            to
        ) == TransitionDirection.DOWN -> slideOutVertically { it } + fadeOut()

        else -> ExitTransition.None
    }
}


fun getPopEnterTransition(initial: NavBackStackEntry, target: NavBackStackEntry): EnterTransition {
    return getEnterTransition(initial, target)
}

fun getPopExitTransition(initial: NavBackStackEntry, target: NavBackStackEntry): ExitTransition {
    return getExitTransition(initial, target)
}

