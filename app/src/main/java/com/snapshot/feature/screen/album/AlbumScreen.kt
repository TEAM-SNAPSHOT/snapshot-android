import android.Manifest
import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.snapshot.SnapShotApplication
import com.snapshot.feature.component.instaShareButton.InstaShareButton
import com.snapshot.res.modifier.ColorTheme
import getSetting.getAlbumName
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    navigateToChooseFrame: () -> Unit,
) {
    val context = SnapShotApplication.getContext()
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val albumName = getAlbumName(context)

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }


    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(permission)
        }
    }

    fun loadPhotosFromAlbum(context: Context, albumName: String): List<Photo> {
        val list = mutableListOf<Photo>()
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val selection = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(albumName)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(collection, projection, selection, selectionArgs, sortOrder)
            ?.use { cursor ->
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val uri = ContentUris.withAppendedId(collection, id)
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)?.let { bmp ->
                        list += Photo(uri, bmp)
                    }
                }
            }
        return list
    }

    if (hasPermission) {
        val images = remember { mutableStateListOf<Photo>() }
        var selectedImage by remember { mutableStateOf<Photo?>(null) }
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            val loadedBitmaps = loadPhotosFromAlbum(context, albumName)
            images.clear()
            images.addAll(loadedBitmaps)
        }
        if (images.isNotEmpty()) {
            Box {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    items(images.size) { index ->
                        val photo = images[index]
                        Image(
                            bitmap = photo.bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .clickable {
                                    selectedImage = photo
                                    scope.launch {
                                        sheetState.show()
                                    }
                                }
                        )
                    }
                }

                selectedImage?.let { photo ->

                    val deleteLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult()
                    ) { result ->
                        if (result.resultCode == Activity.RESULT_OK && selectedImage != null) {
                            // 승인 후 실제 삭제 진행
                            val rowsDeleted = context.contentResolver.delete(selectedImage!!.uri, null, null)
                            if (rowsDeleted > 0) {
                                images.removeIf { it == selectedImage }
                                selectedImage = null
                                scope.launch { sheetState.hide() }
                            }
                        }
                    }

                    fun deleteImage(imageUri: Uri) {
                        try {
                            val rowsDeleted = context.contentResolver.delete(imageUri, null, null)
                            if (rowsDeleted > 0) {
                                images.removeIf { it.uri == imageUri }
                                selectedImage = null
                                scope.launch { sheetState.hide() }
                            }
                        } catch (e: SecurityException) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                                e is RecoverableSecurityException
                            ) {
                                val intentSender = e.userAction.actionIntent.intentSender
                                val request = IntentSenderRequest.Builder(intentSender).build()
                                deleteLauncher.launch(request)
                            } else {
                                e.printStackTrace()
                            }
                        }
                    }

                    ModalBottomSheet(
                        onDismissRequest = {
                            selectedImage = null
                            scope.launch { sheetState.hide() }
                        },
                        sheetState = sheetState,
                        containerColor = ColorTheme.colors.bg,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Image(
                                bitmap = photo.bitmap.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxHeight(0.8f)
                            )
                            InstaShareButton(
                                image = photo.bitmap,
                                context = context
                            )
                            Button(
                                onClick = {
                                    deleteImage(selectedImage!!.uri)
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ColorTheme.colors.main,
                                    contentColor = ColorTheme.colors.font
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(48.dp)
                            ) {
                                Text("삭제하기")
                            }
                        }
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("아직 찍은 사진이 없습니다", color = ColorTheme.colors.font)
                Spacer(modifier = Modifier.size(6.dp))
                Text(modifier = Modifier.clickable { navigateToChooseFrame() },text = "사진 찍으러 가기", color = ColorTheme.colors.main, textDecoration = TextDecoration.Underline, )
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("갤러리 접근 권한이 필요합니다.", color = ColorTheme.colors.font)
        }
    }
}

data class Photo(val uri: Uri, val bitmap: Bitmap)
