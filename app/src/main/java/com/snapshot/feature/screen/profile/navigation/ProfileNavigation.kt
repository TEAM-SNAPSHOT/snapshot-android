package com.snapshot.feature.screen.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.snapshot.feature.screen.profile.ProfileScreen
import com.snapshot.feature.screen.splash.navigation.SPLASH_ROUTE

const val PROFILE_ROUTE = "profile" // -> 현재 화면의 경로

fun NavController.navigateToProfile() { // -> 다른 화면에서 이 화면으로 오고싶을 때 사용하는 함수
    this.navigate(PROFILE_ROUTE)
}
// -> 뒤로가기를 막아야 하는게 아니라면 fun NavController.navigateToHome() = this.navigate(HOME_ROUTE) 이렇게만 적어도 됩니다

fun NavGraphBuilder.profileScreen() { // -> navigateToHome이 호출됐을 떄 무슨 화면을 보여줄지를 결정하는 함수
    composable(
        route = PROFILE_ROUTE,
    ) {
        ProfileScreen()
    }
}