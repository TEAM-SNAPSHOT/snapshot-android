package com.snapshot.feature.screen.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class ProfileViewModel {
    var shotTime by mutableStateOf("8")
        private set

    var albumName by mutableStateOf("스냅샷")
        private set

    fun updateShotTime(newTime: String) {
        shotTime = newTime
    }

    fun updateAlbumName(newName: String) {
        albumName = newName
    }
}