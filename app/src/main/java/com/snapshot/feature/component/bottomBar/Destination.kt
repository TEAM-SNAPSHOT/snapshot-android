package com.snapshot.feature.component.bottomBar

import androidx.compose.runtime.Composable
import com.snapshot.R
import com.snapshot.feature.screen.album.navigation.ALBUM_ROUTE
import com.snapshot.feature.screen.setting.navigation.SETTING_ROUTE

enum class Destination(
    private val iconRes: Int,
    val route: String,
    val label: String
) {
    ALBUM(R.drawable.photo, ALBUM_ROUTE, "앨범"),
    SETTING(R.drawable.setting, SETTING_ROUTE, "설정");

    @Composable
    fun getIcon(): Int {
        return iconRes
    }
}