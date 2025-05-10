package com.snapshot.feature.screen.album.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.snapshot.feature.screen.album.AlbumScreen

const val ALBUM_ROUTE = "album"

// -> HomeNavigation과 다르게 스플래시 화면으로 이동할 필요가 없다면 적지 않아도 됩니다

fun NavGraphBuilder.albumScreen() {
    composable(
        route = ALBUM_ROUTE,
    ) {
        AlbumScreen(albumName = "스크린샷")
    }
}