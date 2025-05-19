package com.snapshot.feature.component.instaShareButton

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import com.snapshot.R
import com.snapshot.res.modifier.ColorTheme
import java.io.File
import java.io.FileOutputStream

@Composable
fun InstaShareButton(image : Bitmap, context: Context, modifier: Modifier = Modifier) {
    // Bitmap → Uri 변환
    fun bitmapToUri(bitmap: Bitmap): Uri {
        val file = File(context.getExternalFilesDir(null), "selected_image.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val imageUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        return imageUri
    }

    val backgroundUri = createWhiteBackgroundUri(context)
    val file = File(context.getExternalFilesDir(null), "shared_image.jpg")
    val inputStream = context.contentResolver.openInputStream(bitmapToUri(image))
    val outputStream = file.outputStream()
    inputStream?.copyTo(outputStream)
    inputStream?.close()
    outputStream.close()

    val imageUriForInstagram = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
    val interactionSource = remember { MutableInteractionSource() }

    Button(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(48.dp),
        onClick = { shareToInstagramStory(context, backgroundUri, imageUriForInstagram) },
        enabled = true,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorTheme.colors.main,
            contentColor = Color.White
        ),
        interactionSource = interactionSource,
        content = {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "스토리에 공유하기",
                    color = ColorTheme.colors.white,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Image(
                    painter = painterResource(id = R.drawable.baseline_ios_share_24  ),
                    contentDescription = "공유 아이콘",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

fun shareToInstagramStory(
    context: Context,
    backgroundUri : Uri,
    imageUri: Uri,
    fbAppId: String = "1409624230493146"
) {
    val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
        setDataAndType(backgroundUri, getMimeType(backgroundUri) ?: "image/jpeg")
        putExtra("source_application", fbAppId)
        putExtra("interactive_asset_uri", imageUri)
        // ✅ 배경색 설정 (선택 사항)
        putExtra("top_background_color", "#FFEB3B")
        putExtra("bottom_background_color", "#FFC107")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)

        // ✅ 패키지 명시
        setPackage("com.instagram.android")
    }

    // ✅ URI 권한 명시적으로 부여
    context.grantUriPermission(
        "com.instagram.android",
        imageUri,
        Intent.FLAG_GRANT_READ_URI_PERMISSION
    )

    val packageManager = context.packageManager
    if (intent.resolveActivity(packageManager) != null) {
        context.startActivity(intent)
    } else {
        android.widget.Toast.makeText(context, "Instagram이 설치되어 있지 않습니다.", android.widget.Toast.LENGTH_SHORT).show()
    }
}



// 이미지 MIME 타입 가져오기
fun getMimeType(uri: Uri): String? {
    val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}

fun createWhiteBackgroundUri(context: Context): Uri {
    val width = 1080
    val height = 1920

    // 1. Bitmap 생성 (검정색)
    val blackBitmap = createBitmap(width, height)
    val canvas = Canvas(blackBitmap)
    canvas.drawColor(0xFFFFFFFF.toInt())

    // 2. File 생성
    val file = File(context.getExternalFilesDir(null), "black_background.png")
    val outputStream = FileOutputStream(file)
    blackBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    // 3. Uri 반환
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}
