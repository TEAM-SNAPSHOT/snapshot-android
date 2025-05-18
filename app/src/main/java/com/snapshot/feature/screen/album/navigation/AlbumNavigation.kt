package com.snapshot.feature.screen.album.navigation

import AlbumScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val ALBUM_ROUTE = "album"

fun NavController.navigateToAlbum() {
    this.navigate(ALBUM_ROUTE)
}

fun NavGraphBuilder.albumScreen(
    navigateToPhoto: () -> Unit,
) {
    composable(
        route = ALBUM_ROUTE,
    ) {
        AlbumScreen(navigateToPhoto = navigateToPhoto,)
    }
}