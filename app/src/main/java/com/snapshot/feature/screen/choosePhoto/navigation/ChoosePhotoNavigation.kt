package com.snapshot.feature.screen.choosePhoto.navigation

import android.graphics.Bitmap
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.snapshot.feature.screen.chooseFrame.ChooseFrameScreen
import com.snapshot.feature.screen.choosePhoto.ChoosePhotoScreen
import com.snapshot.feature.screen.photo.viewModel.PhotoViewModel

const val CHOOSE_PHOTO_ROUTE = "choose_photo"

fun NavController.navigateToChoosePhoto() {
    this.navigate(CHOOSE_PHOTO_ROUTE)
}

fun NavGraphBuilder.choosePhotoScreen(
    viewModel: PhotoViewModel,
    navigateToPhoto: () -> Unit,
    navigateToFilter: () -> Unit
) {
    composable(
        route = CHOOSE_PHOTO_ROUTE
    ) {
        ChoosePhotoScreen(
            viewModel = viewModel,
            navigateToPhoto = navigateToPhoto,
            navigateToFilter = navigateToFilter
        )
    }
}