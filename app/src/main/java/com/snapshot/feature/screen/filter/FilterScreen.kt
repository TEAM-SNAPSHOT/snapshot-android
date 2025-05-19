package com.snapshot.feature.screen.filter

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.snapshot.feature.screen.photo.viewModel.PhotoViewModel
import com.snapshot.res.modifier.ColorTheme
import com.snapshot.res.modifier.pressEffect
import getSetting.getAlbumName
import kotlinx.coroutines.delay


@Composable
fun FilterScreen(
    viewModel: PhotoViewModel,
    navigateToInsta: () -> Unit,
) {
    val context = LocalContext.current
    val imageList = viewModel.getSelectedPhoto()

    var filterType by remember { mutableStateOf(FilterType.ORIGINAL) }
    var startCapture by remember { mutableStateOf(false) }

    val filterBoxRef = remember { mutableStateOf<ComposeView?>(null) }

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

    LaunchedEffect(startCapture) {
        if (startCapture) {
            delay(200)
            val view = filterBoxRef.value
            if (view != null) {
                val bitmap = captureView(view)
                saveBitmapToGallery(context, bitmap, getAlbumName(context))
                viewModel.croppedImage = bitmap
                Toast.makeText(context, "저장되었습니다", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "이미지를 캡처할 수 없습니다", Toast.LENGTH_SHORT).show()
            }

            navigateToInsta()
            startCapture = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ColorTheme.colors.bg)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center),
        ) {
            key(filterType) {
                AndroidView(
                    factory = { ctx ->
                        ComposeView(ctx).apply {
                            filterBoxRef.value = this
                            setContent {
                                FilterContent(
                                    imageList = imageList,
                                    filter = currentFilter,
                                    frame = viewModel.selectedFrame!!,
                                    selectedIndex = viewModel.selectedFrameIndex.intValue
                                )
                            }
                        }
                    },
                    modifier = Modifier.wrapContentSize()
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
                        startCapture = true
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
fun FilterContent(
    imageList: List<ImageBitmap>,
    filter: ColorFilter?,
    frame: Painter,
    selectedIndex: Int
) {
    Box {
        when {
            selectedIndex <= 7 -> {
                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 5.dp)
                ) {
                    Column {
                        Spacer(Modifier.height(86.dp))
                        FilteredImage(bitmap = imageList[0], filter = filter)
                        Spacer(Modifier.height(10.dp))
                        FilteredImage(bitmap = imageList[1], filter = filter)
                    }
                    Spacer(Modifier.width(2.dp))
                    Column {
                        Spacer(Modifier.height(6.dp))
                        FilteredImage(bitmap = imageList[2], filter = filter)
                        Spacer(Modifier.height(4.dp))
                        FilteredImage(bitmap = imageList[3], filter = filter)
                    }
                }
            }

            selectedIndex <= 15 -> {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 8.dp)
                        .padding(top = 10.dp)
                ) {
                    Column {
                        FilteredImage(bitmap = imageList[0], filter = filter)
                        Spacer(Modifier.height(8.dp))
                        FilteredImage(bitmap = imageList[1], filter = filter)
                    }
                    Spacer(Modifier.width(2.dp))
                    Column {
                        FilteredImage(bitmap = imageList[2], filter = filter)
                        Spacer(Modifier.height(8.dp))
                        FilteredImage(bitmap = imageList[3], filter = filter)
                    }
                }
            }

            else -> {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 8.dp)
                        .padding(top = 10.dp)
                ) {
                    Column {
                        Spacer(Modifier.height(4.dp))
                        FilteredImage(bitmap = imageList[0], filter = filter, isVertical = true)
                        Spacer(Modifier.height(12.dp))
                        FilteredImage(bitmap = imageList[1], filter = filter, isVertical = true)
                        Spacer(Modifier.height(12.dp))
                        FilteredImage(bitmap = imageList[2], filter = filter, isVertical = true)
                    }
                }
            }
        }

        Image(
            painter = frame,
            contentDescription = null,
            modifier = Modifier
                .width(250.dp)
                .height(402.28.dp)
                .align(alignment = Alignment.TopCenter)
        )
    }
}

@Composable
fun FilteredImage(
    bitmap: ImageBitmap,
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

fun captureView(view: View): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

enum class FilterType {
    ORIGINAL, GRAYSCALE, BRIGHTNESS
}

fun saveBitmapToGallery(context: Context, bitmap: Bitmap, albumName: String = "snapShot") {
    val filename = "IMG_${System.currentTimeMillis()}.png"
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$albumName")
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val contentResolver = context.contentResolver
    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        contentResolver.openOutputStream(uri).use { outputStream ->
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
        }

        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        contentResolver.update(uri, contentValues, null, null)
    }
}




