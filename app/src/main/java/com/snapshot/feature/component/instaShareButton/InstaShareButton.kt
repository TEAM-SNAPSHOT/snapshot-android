package com.snapshot.feature.component.instaShareButton

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.snapshot.res.modifier.ColorTheme
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.createBitmap

@Composable
fun InstaShareButton(uri : Uri, context: Context) {
    val backgroundUri = createBlackBackgroundUri(context)
    val file = File(context.getExternalFilesDir(null), "shared_image.jpg")
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = file.outputStream()
    inputStream?.copyTo(outputStream)
    inputStream?.close()
    outputStream.close()

    val imageUriForInstagram = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
    Button(
        modifier = Modifier.fillMaxWidth(0.9f).height(48.dp),
        onClick = { shareToInstagramStory(context, backgroundUri ,uri) },
        enabled = true,
        shape = RoundedCornerShape(12.dp), // ✅ 모서리 둥글게 (radius)
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorTheme.colors.main, // 버튼 배경색
            contentColor = Color.White         // 텍스트 색
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        border = BorderStroke(0.dp, ColorTheme.colors.main),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp), // ✅ 내부 여백
        interactionSource = remember { MutableInteractionSource() }, // ✅ 터치/포커스 추적
        content = {
            Text(text = "스토리에 공유하기")
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

fun createBlackBackgroundUri(context: Context): Uri {
    val width = 1080
    val height = 1920

    // 1. Bitmap 생성 (검정색)
    val blackBitmap = createBitmap(width, height)
    val canvas = Canvas(blackBitmap)
    canvas.drawColor(0x00000000)

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
