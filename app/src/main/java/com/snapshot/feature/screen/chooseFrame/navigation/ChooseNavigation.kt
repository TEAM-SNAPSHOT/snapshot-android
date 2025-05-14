package com.snapshot.feature.screen.chooseFrame.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.snapshot.feature.screen.chooseFrame.ChooseFrameScreen

const val CHOOSE_FRAME_ROUTE = "chooseFrame" 

fun NavController.navigateToChooseFrame() = this.navigate(CHOOSE_FRAME_ROUTE)

fun NavGraphBuilder.chooseFrameScreen(
    navigateToPhoto: () -> Unit
) {
    composable(
        route = CHOOSE_FRAME_ROUTE,
    ) {
        ChooseFrameScreen(navigateToPhoto = navigateToPhoto)
    }
}