package com.snapshot.feature.screen.filter.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.snapshot.feature.screen.chooseFrame.ChooseFrameScreen
import com.snapshot.feature.screen.filter.FilterScreen
import com.snapshot.feature.screen.photo.viewModel.PhotoViewModel

const val FILTER_ROUTE = "filter"

fun NavController.navigateToFilter() = this.navigate(FILTER_ROUTE)

fun NavGraphBuilder.filterScreen(
    viewModel: PhotoViewModel,
    navigateToAlbum: () -> Unit,
) {
    composable(
        route = FILTER_ROUTE,
    ) {
        FilterScreen(
            viewModel = viewModel,
            navigateToAlbum = navigateToAlbum,
        )
    }
}