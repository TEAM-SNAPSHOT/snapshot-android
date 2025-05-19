package com.snapshot.feature.screen.photo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snapshot.R
import com.snapshot.SnapShotApplication
import com.snapshot.feature.screen.photo.viewModel.PhotoViewModel
import com.snapshot.res.modifier.ColorTheme
import com.snapshot.res.modifier.coiny
import com.snapshot.res.modifier.pressEffect
import dagger.hilt.android.qualifiers.ApplicationContext
import getSetting.getShotTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.provider.Settings
import android.widget.Toast


@Composable
fun PhotoScreen(
    navigateToChoosePhoto: () -> Unit,
    navigateToAlbum: () -> Unit,
    viewModel: PhotoViewModel
) {
    val context = SnapShotApplication.getContext()
    val delay = getShotTime(context)
    var isTimerEnabled by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val cameraPermission = Manifest.permission.CAMERA
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                cameraPermission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(cameraPermission)
        }
    }


    var countdownValue by remember { mutableIntStateOf(0) }
    var isCountingDown by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val previewView = remember { PreviewView(context) }
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    val capturedBitmaps = viewModel.capturedPhotos

    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }

    val cameraSelector = remember(lensFacing) {
        CameraSelector.Builder().requireLensFacing(lensFacing).build()
    }



    LaunchedEffect(lensFacing) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner, cameraSelector, preview, imageCapture
        )
    }

    LaunchedEffect(capturedBitmaps.size) {
        Log.d("photo", "PhotoScreen: ")
        if (capturedBitmaps.size == 8) {
            navigateToChoosePhoto()
        }
    }

    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

            if (isCountingDown && countdownValue > 0 && isTimerEnabled) {
                Text(
                    text = countdownValue.toString(),
                    color = Color.White,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(0.8f)
                )
            }

            Row(
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(top = 24.dp, end = 12.dp)
                    .align(alignment = Alignment.TopEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isTimerEnabled) {
                    Text(
                        text = delay,
                        color = Color.White,
                        fontSize = 30.sp
                    )
                }

                Icon(
                    painter = painterResource(if (isTimerEnabled) R.drawable.timer else R.drawable.timer_off),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .pressEffect(
                            onClick = {
                                isTimerEnabled = !isTimerEnabled
                                Toast.makeText(context, "타이머가 비활성화되었습니다!", Toast.LENGTH_SHORT).show()
                            }
                        ),
                    tint = Color.White
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .padding(horizontal = 24.dp)
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.logout_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = ColorTheme.colors.exitIcon),
                    modifier = Modifier
                        .width(32.dp)
                        .pressEffect(
                            onClick = {
                                viewModel.clearPhotos()
                                navigateToAlbum()
                            }
                        ),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .height(70.dp)
                        .width(154.dp)
                        .pressEffect(
                            enable = capturedBitmaps.size < 8 && !isCountingDown,
                            onClick = {
                                val shotDelay = if (isTimerEnabled) delay.toIntOrNull() ?: 3 else 0

                                if (shotDelay == 0) {
                                    imageCapture.takePicture(
                                        ContextCompat.getMainExecutor(context),
                                        object : ImageCapture.OnImageCapturedCallback() {
                                            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                                                val bitmap = imageProxy.toBitmap().let {
                                                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                                                    val rotated = it.rotate(rotationDegrees)
                                                    if (viewModel.selectedFrameIndex.intValue >= 16) {
                                                        rotated.rotate(90)
                                                    } else {
                                                        rotated
                                                    }
                                                }
                                                imageProxy.close()
                                                viewModel.addPhoto(bitmap)
                                            }



                                            override fun onError(exception: ImageCaptureException) {
                                                exception.printStackTrace()
                                            }
                                        }
                                    )
                                } else {
                                    isCountingDown = true
                                    countdownValue = shotDelay

                                    coroutineScope.launch {
                                        for (i in shotDelay downTo 1) {
                                            countdownValue = i
                                            delay(1000L)
                                        }
                                        countdownValue = 0
                                        isCountingDown = false

                                        imageCapture.takePicture(
                                            ContextCompat.getMainExecutor(context),
                                            object : ImageCapture.OnImageCapturedCallback() {
                                                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                                                    val bitmap = imageProxy.toBitmap().let {
                                                        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                                                        val rotated = it.rotate(rotationDegrees)
                                                        if (viewModel.selectedFrameIndex.intValue >= 16) {
                                                            rotated.rotate(90)
                                                        } else {
                                                            rotated
                                                        }
                                                    }
                                                    imageProxy.close()
                                                    viewModel.addPhoto(bitmap)
                                                }



                                                override fun onError(exception: ImageCaptureException) {
                                                    exception.printStackTrace()
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        )

                        .background(ColorTheme.colors.main, RoundedCornerShape(20.dp))
                        .padding(horizontal = 18.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (capturedBitmaps.isEmpty()) "GO!" else "${capturedBitmaps.size}/8",
                            style = coiny,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                        Spacer(Modifier.width(16.dp))
                        Image(
                            painter = painterResource(R.drawable.snapshot_icon),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                        )
                    }
                }
                Image(
                    painter = painterResource(R.drawable.flip_camera_ios_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = Color.White),
                    modifier = Modifier
                        .width(36.dp)
                        .pressEffect(
                            onClick = {
                                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                                    CameraSelector.LENS_FACING_FRONT
                                } else {
                                    CameraSelector.LENS_FACING_BACK
                                }
                            }
                        ),
                    contentScale = ContentScale.Crop
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(32.dp)
            ) {
                Text(
                    text = "카메라 권한이 필요합니다.",
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .pressEffect(
                            onClick = {
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:${context.packageName}")
                                ).apply {
                                    addCategory(Intent.CATEGORY_DEFAULT)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(intent)
                            }
                        )
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(
                            color = ColorTheme.colors.main,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .height(54.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "설정으로 이동",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

fun Bitmap.rotate(degrees: Int): Bitmap {
    val matrix = Matrix().apply {
        postRotate(degrees.toFloat())
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}






