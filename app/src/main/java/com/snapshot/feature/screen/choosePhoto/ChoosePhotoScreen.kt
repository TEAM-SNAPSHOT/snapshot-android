package com.snapshot.feature.screen.choosePhoto

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snapshot.SnapShotApplication
import com.snapshot.feature.screen.photo.viewModel.PhotoViewModel
import com.snapshot.res.modifier.ColorTheme
import com.snapshot.res.modifier.coiny
import com.snapshot.res.modifier.pressEffect


@Composable
fun ChoosePhotoScreen(
    viewModel: PhotoViewModel,
    navigateToPhoto: () -> Unit,
    navigateToFilter: () -> Unit
) {
    val photoBitmaps = viewModel.capturedPhotos
    val selectedPhotosOrder = viewModel.selectedPhotosOrder

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .background(color = ColorTheme.colors.bg),
        topBar = {
            Box(
                modifier = Modifier
                    .background(color = ColorTheme.colors.bg)
            )
        },
        bottomBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = ColorTheme.colors.bg)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .pressEffect(
                            onClick = {
                                viewModel.clearPhotos()
                                navigateToPhoto()
                            }
                        )
                        .background(
                            color = ColorTheme.colors.revBg,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .height(54.dp)
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "다시 찍기",
                        color = ColorTheme.colors.grayText,
                        fontSize = 20.sp
                    )
                }
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .pressEffect(
                            onClick = {
                                navigateToFilter()
                            }
                        )
                        .background(
                            color = ColorTheme.colors.main,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .height(54.dp)
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "선택 완료!",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .background(color = ColorTheme.colors.bg)
                .padding(8.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Let's make a SNAPSHOT!",
                fontSize = 24.sp,
                color = ColorTheme.colors.font,
                style = coiny
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "사진을 선택해주세요 (최대 4개)",
                fontSize = 14.sp,
                color = ColorTheme.colors.font
            )
            Spacer(Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(
                    count = photoBitmaps.size,
                    key = { it }
                ) { index ->
                    val isSelected = selectedPhotosOrder.contains(index)
                    val selectionOrder = viewModel.getSelectionOrder(index)

                    key(index, isSelected, selectionOrder) {
                        OptimizedPhotoItem(
                            bitmap = photoBitmaps[index],
                            index = index,
                            isSelected = isSelected,
                            selectionOrder = selectionOrder,
                            onPhotoSelected = {
                                viewModel.selectPhoto(it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OptimizedPhotoItem(
    bitmap: Bitmap,
    index: Int,
    isSelected: Boolean,
    selectionOrder: Int?,
    onPhotoSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(3/4f)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (isSelected) Color.Gray.copy(alpha = 0.2f) else Color.Transparent,
                    RoundedCornerShape(8.dp)
                )
                .then(
                    if (isSelected) Modifier.border(2.dp, ColorTheme.colors.main, RoundedCornerShape(8.dp))
                    else Modifier
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onPhotoSelected(index)
                }
        ) {
            val imageBitmap = remember(bitmap) { bitmap.asImageBitmap() }
            Image(
                bitmap = imageBitmap,
                contentDescription = "Photo $index",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (isSelected && selectionOrder != null) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .background(ColorTheme.colors.main, CircleShape)
                ) {
                    Text(
                        text = "$selectionOrder",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
