package com.snapshot

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.snapshot.feature.component.navigation.BottomNavigationBar
import com.snapshot.feature.component.topbar.TopBar
import com.snapshot.feature.screen.home.navigation.homeScreen
import com.snapshot.feature.screen.home.navigation.navigateToHome
import com.snapshot.feature.screen.setting.navigation.settingScreen
import com.snapshot.feature.screen.splash.navigation.SPLASH_ROUTE
import com.snapshot.feature.screen.splash.navigation.splashScreen
import com.snapshot.res.modifier.AppTheme
import com.snapshot.res.modifier.ColorTheme


@Composable
fun App(navHostController: NavHostController = rememberNavController()) {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                modifier = Modifier.systemBarsPadding(),
                topBar = { TopBar() },
                bottomBar = {
                    BottomNavigationBar(navHostController)
                },
                containerColor = ColorTheme.colors.bg
            ) { innerPadding ->
                NavHost(
                    navController = navHostController,
                    startDestination = SPLASH_ROUTE,
                    modifier = Modifier.padding(innerPadding),
                    enterTransition = { getEnterTransition(initialState, targetState) },
                    exitTransition = { getExitTransition(initialState, targetState) },
                    popEnterTransition = { getPopEnterTransition(initialState, targetState) },
                    popExitTransition = { getPopExitTransition(initialState, targetState) },
                ) {
                    splashScreen(
                        navigateToHome = navHostController::navigateToHome
                    )
                    homeScreen()
                    settingScreen()
                }
            }
        }
    }
}


enum class TransitionDirection {
    LEFT, RIGHT, UP, DOWN, CUSTOM
}

private val transitionMap = mapOf(
    "home" to mapOf(
        "profile" to TransitionDirection.RIGHT,
        "move" to TransitionDirection.LEFT
    ),
    "profile" to mapOf(
        "home" to TransitionDirection.LEFT,
        "move" to TransitionDirection.LEFT
    ),
    "move" to mapOf(
        "home" to TransitionDirection.RIGHT,
        "profile" to TransitionDirection.RIGHT,
        "signMove" to TransitionDirection.CUSTOM
    ),
    "signMove" to mapOf(
        "home" to TransitionDirection.RIGHT,
        "profile" to TransitionDirection.RIGHT,
        "move" to TransitionDirection.CUSTOM
    )
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
        getTransitionDirection(from, to) == TransitionDirection.LEFT -> slideInHorizontally { -it } + fadeIn()
        getTransitionDirection(from, to) == TransitionDirection.RIGHT -> slideInHorizontally { it } + fadeIn()
        getTransitionDirection(from, to) == TransitionDirection.UP -> slideInVertically { -it } + fadeIn()
        getTransitionDirection(from, to) == TransitionDirection.DOWN -> slideInVertically { it } + fadeIn()
        else -> EnterTransition.None
    }
}

fun getExitTransition(initial: NavBackStackEntry, target: NavBackStackEntry): ExitTransition {
    val from = initial.destination.route
    val to = target.destination.route

    return when {
        from == "move" && to == "signMove" -> slideOutVertically { -it } + fadeOut()
        from == "signMove" && to == "move" -> slideOutVertically { it } + fadeOut()
        getTransitionDirection(from, to) == TransitionDirection.LEFT -> slideOutHorizontally { it } + fadeOut()
        getTransitionDirection(from, to) == TransitionDirection.RIGHT -> slideOutHorizontally { -it } + fadeOut()
        getTransitionDirection(from, to) == TransitionDirection.UP -> slideOutVertically { -it } + fadeOut()
        getTransitionDirection(from, to) == TransitionDirection.DOWN -> slideOutVertically { it } + fadeOut()
        else -> ExitTransition.None
    }
}



fun getPopEnterTransition(initial: NavBackStackEntry, target: NavBackStackEntry): EnterTransition {
    return getEnterTransition(initial, target)
}

fun getPopExitTransition(initial: NavBackStackEntry, target: NavBackStackEntry): ExitTransition {
    return getExitTransition(initial, target)
}

