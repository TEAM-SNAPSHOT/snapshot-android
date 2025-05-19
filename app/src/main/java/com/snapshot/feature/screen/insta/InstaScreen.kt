package com.snapshot.feature.screen.insta

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.snapshot.R
import com.snapshot.SnapShotApplication
import com.snapshot.feature.component.instaShareButton.InstaShareButton
import com.snapshot.feature.screen.photo.viewModel.PhotoViewModel
import com.snapshot.res.modifier.ColorTheme
import com.snapshot.res.modifier.pressEffect


@Composable
fun InstaScreen(
    navigateToAlbum: () -> Unit,
    viewModel: PhotoViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ColorTheme.colors.bg)
            .padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.close_icon),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 40.dp)
                .size(40.dp)
                .pressEffect(
                    onClick = {
                        navigateToAlbum()
                    }
                ),
            colorFilter = ColorFilter.tint(ColorTheme.colors.revBg)
        )

        viewModel.croppedImage?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .width(250.dp)
                    .height(402.28.dp)
                    .align(alignment = Alignment.Center)
            )
        }
        viewModel.croppedImage?.let { InstaShareButton(it, SnapShotApplication.getContext(), Modifier.align(
            Alignment.BottomCenter)
            .padding(bottom = 10.dp)) }
    }
}