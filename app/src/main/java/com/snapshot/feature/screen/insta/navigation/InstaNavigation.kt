package com.snapshot.feature.screen.insta.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.snapshot.feature.screen.insta.InstaScreen
import com.snapshot.feature.screen.photo.viewModel.PhotoViewModel

const val INSTA_ROUTE = "insta"

fun NavController.navigateToInsta() = this.navigate(INSTA_ROUTE)

fun NavGraphBuilder.instaScreen(
    navigateToAlbum: () -> Unit,
    viewModel: PhotoViewModel
) {
    composable(
        route = INSTA_ROUTE,
    ) {
        InstaScreen(
            navigateToAlbum = navigateToAlbum,
            viewModel = viewModel
        )
    }
}