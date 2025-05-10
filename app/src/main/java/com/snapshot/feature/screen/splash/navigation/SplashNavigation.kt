package com.snapshot.feature.screen.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.snapshot.feature.screen.splash.SplashScreen

const val SPLASH_ROUTE = "profile"

// -> HomeNavigation과 다르게 스플래시 화면으로 이동할 필요가 없다면 적지 않아도 됩니다

fun NavGraphBuilder.splashScreen(
    navigateToHome: () -> Unit, // -> 스플래시 화면에서 다른 화면으로 이동하고 싶을 때 사용하는 함수
) {
    composable(
        route = SPLASH_ROUTE,
    ) {
        SplashScreen(
            navigateToHome = navigateToHome,
        )
    }
}