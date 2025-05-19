package com.snapshot.feature.screen.filter

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snapshot.SnapShotApplication
import com.snapshot.feature.screen.photo.viewModel.PhotoViewModel
import com.snapshot.res.modifier.ColorTheme
import com.snapshot.res.modifier.pressEffect
import getSetting.getAlbumName


@Composable
fun FilterScreen(
    viewModel: PhotoViewModel,
    navigateToAlbum: () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as Activity
    val imageList = viewModel.getSelectedPhoto()

    var croppedImage by remember { mutableStateOf<Bitmap?>(null) }
    val viewRef = remember { mutableStateOf<View?>(null) }

    var filterType by remember { mutableStateOf(FilterType.ORIGINAL) }

    val grayscaleMatrix = ColorMatrix().apply { setToSaturation(0f) }
    val brightnessMatrix = ColorMatrix(
        floatArrayOf(
            1.4f, 0f, 0f, 0f, 0f,
            0f, 1.4f, 0f, 0f, 0f,
            0f, 0f, 1.4f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )

    val currentFilter = remember(filterType) {
        when (filterType) {
            FilterType.GRAYSCALE -> ColorFilter.colorMatrix(grayscaleMatrix)
            FilterType.BRIGHTNESS -> ColorFilter.colorMatrix(brightnessMatrix)
            else -> null
        }
    }

    LaunchedEffect(Unit) {
        val rootView = activity.window.decorView.rootView
        viewRef.value = rootView
        val cropped = captureAndCropFilterScreen(rootView, context, isVertical = viewModel.selectedFrameIndex.intValue >= 16)
        croppedImage = cropped
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF8F8F8))
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center),
        ) {
            Box(
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(top = 20.dp)
            ) {
                if (viewModel.selectedFrameIndex.intValue <= 7) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(bottom = 5.dp)
                    ) {
                        Column {
                            Spacer(Modifier.height(86.dp))
                            FilteredImage(bitmap = imageList[0], filter = currentFilter)
                            Spacer(Modifier.height(10.dp))
                            FilteredImage(bitmap = imageList[1], filter = currentFilter)
                        }
                        Spacer(Modifier.width(2.dp))
                        Column {
                            Spacer(Modifier.height(6.dp))
                            FilteredImage(bitmap = imageList[2], filter = currentFilter)
                            Spacer(Modifier.height(4.dp))
                            FilteredImage(bitmap = imageList[3], filter = currentFilter)
                        }
                    }
                } else if (viewModel.selectedFrameIndex.intValue <= 15) {
                    Row(
                        modifier = Modifier
                            .align(alignment = Alignment.TopCenter)
                            .padding(horizontal = 8.dp)
                            .padding(top = 10.dp)
                    ) {
                        Column {
                            FilteredImage(bitmap = imageList[0], filter = currentFilter)
                            Spacer(Modifier.height(8.dp))
                            FilteredImage(bitmap = imageList[1], filter = currentFilter)
                        }
                        Spacer(modifier = Modifier.width(2.dp))
                        Column {
                            FilteredImage(bitmap = imageList[2], filter = currentFilter)
                            Spacer(Modifier.height(8.dp))
                            FilteredImage(bitmap = imageList[3], filter = currentFilter)
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .align(alignment = Alignment.TopCenter)
                            .padding(horizontal = 8.dp)
                            .padding(top = 10.dp)
                    ) {
                        Column {
                            Spacer(Modifier.height(4.dp))
                            FilteredImage(bitmap = imageList[0], filter = currentFilter, isVertical = true)
                            Spacer(Modifier.height(12.dp))
                            FilteredImage(bitmap = imageList[1], filter = currentFilter, isVertical = true)
                            Spacer(Modifier.height(12.dp))
                            FilteredImage(bitmap = imageList[2], filter = currentFilter, isVertical = true)
                        }
                    }
                }
                Image(
                    painter = viewModel.selectedFrame!!,
                    contentDescription = null,
                    modifier = Modifier
                        .width(250.dp)
                        .height(402.28.dp)
                        .align(alignment = Alignment.TopCenter)
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FilterButton("원본", Color(0xFFFEDAB8), filterType == FilterType.ORIGINAL) {
                    filterType = FilterType.ORIGINAL
                }
                FilterButton("흑백", Color(0xFF888888), filterType == FilterType.GRAYSCALE) {
                    filterType = FilterType.GRAYSCALE
                }
                FilterButton("밝게", Color(0xFFEAEAEA), filterType == FilterType.BRIGHTNESS) {
                    filterType = FilterType.BRIGHTNESS
                }
            }

        }
        Box(
            modifier = Modifier
                .pressEffect(
                    onClick = {
                        val rootView = viewRef.value
                        if (rootView != null) {
                            val newCapture = captureAndCropFilterScreen(
                                rootView,
                                context,
                                isVertical = viewModel.selectedFrameIndex.intValue >= 16
                            )
                            saveBitmapToGallery(context, newCapture, getAlbumName(context))
                            Toast.makeText(context, "저장되었습니다", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "이미지를 캡처할 수 없습니다", Toast.LENGTH_SHORT).show()
                        }

                        navigateToAlbum()
                    }
                )

                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    color = ColorTheme.colors.main,
                    shape = RoundedCornerShape(16.dp)
                )
                .align(alignment = Alignment.BottomCenter)
                .height(54.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "저장하기",
                color = Color.White,
                fontSize = 20.sp
            )
        }

    }
}

@Composable
fun FilteredImage(
    bitmap: androidx.compose.ui.graphics.ImageBitmap,
    filter: ColorFilter?,
    isVertical: Boolean = false
) {
    Image(
        bitmap = bitmap,
        contentDescription = null,
        modifier = Modifier
            .width(if (isVertical) 144.dp else 120.dp)
            .height(if (isVertical) 108.dp else 147.dp),
        colorFilter = filter
    )
}


@Composable
fun FilterButton(text: String, color: Color, selected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color = color, shape = CircleShape)
                .border(
                    width = if (selected) 2.dp else 0.dp,
                    color = ColorTheme.colors.main,
                    shape = CircleShape
                )
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF8F8E94)
        )
    }
}

fun captureAndCropFilterScreen(view: View, context: Context, isVertical: Boolean): Bitmap {
    val screenshot = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(screenshot)
    view.draw(canvas)

    val density = context.resources.displayMetrics.density
    val cropWidthPx = ((if (isVertical) 164 else 248) * density).toInt()
    val cropHeightPx = (400 * density).toInt()

    val centerX = screenshot.width / 2
    val startX = (centerX - cropWidthPx / 2).coerceAtLeast(0)

    val additionalOffsetDp = 102
    val startY = ((20 + additionalOffsetDp) * density).toInt()

    val finalWidth = minOf(cropWidthPx, screenshot.width - startX)
    val finalHeight = minOf(cropHeightPx, screenshot.height - startY)

    return Bitmap.createBitmap(screenshot, startX, startY, finalWidth, finalHeight)
}


enum class FilterType {
    ORIGINAL, GRAYSCALE, BRIGHTNESS
}

fun saveBitmapToGallery(context: Context, bitmap: Bitmap, albumName: String = "snapShot") {
    val filename = "IMG_${System.currentTimeMillis()}.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$albumName")
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val contentResolver = context.contentResolver
    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        contentResolver.openOutputStream(uri).use { outputStream ->
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        }

        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        contentResolver.update(uri, contentValues, null, null)
    }
}




