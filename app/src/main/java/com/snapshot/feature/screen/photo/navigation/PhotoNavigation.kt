package com.snapshot.feature.screen.photo.navigation

import android.graphics.Bitmap
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.snapshot.feature.screen.chooseFrame.ChooseFrameScreen
import com.snapshot.feature.screen.choosePhoto.ChoosePhotoScreen
import com.snapshot.feature.screen.photo.PhotoScreen
import com.snapshot.feature.screen.photo.viewModel.PhotoViewModel

const val PHOTO_ROUTE = "photo"

fun NavController.navigateToPhoto() = this.navigate(PHOTO_ROUTE)

fun NavGraphBuilder.photoScreen(
    navigateToChoosePhoto: () -> Unit,
    navigateToAlbum: () -> Unit,
    viewModel: PhotoViewModel
) {
    composable(
        route = PHOTO_ROUTE,
    ) {
        PhotoScreen(
            navigateToChoosePhoto = navigateToChoosePhoto,
            navigateToAlbum = navigateToAlbum,
            viewModel = viewModel
        )
    }
}