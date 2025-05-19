package com.snapshot.feature.screen.photo.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel


class PhotoViewModel : ViewModel() {
    private val _capturedPhotos = mutableStateListOf<Bitmap>()
    val capturedPhotos: List<Bitmap> = _capturedPhotos

    var selectedPhotoIndex by mutableStateOf<Int?>(null)

    var selectedFrame by mutableStateOf<Painter?>(null)

    private val _selectedPhotosOrder = mutableStateListOf<Int>()
    val selectedPhotosOrder: List<Int> = _selectedPhotosOrder

    private val maxSelectablePhotos: Int
        get() = if (selectedFrameIndex.intValue >= 16) 3 else 4


    var selectedFrameIndex = mutableIntStateOf(-1)

    var croppedImage by mutableStateOf<Bitmap?>(null)



    fun selectFrameIndex(index: Int) {
        selectedFrameIndex = mutableIntStateOf(index)
    }

    fun reset() {
        _capturedPhotos.clear()
        _selectedPhotosOrder.clear()
        selectedPhotoIndex = null
        selectedFrame = null
        selectedFrameIndex.intValue = -1
    }

    fun getSelectedPhoto(): MutableList<ImageBitmap> {
        val selectedImage = mutableStateListOf<ImageBitmap>()
        for (item in selectedPhotosOrder) {
            selectedImage += capturedPhotos[item].asImageBitmap()
        }
        return selectedImage
    }


    fun addPhoto(bitmap: Bitmap) {
        try {
            if (_capturedPhotos.size < 8) {
                val resizedBitmap = resizeBitmap(bitmap, 800)
                val rotatedOrCroppedBitmap = if (selectedFrameIndex.intValue >= 16) {
                    resizeToLandscape(resizedBitmap)
                } else {
                    resizedBitmap
                }
                _capturedPhotos.add(rotatedOrCroppedBitmap)
            }
        } catch (e: Exception) {
            Log.e("PhotoViewModel", "Error adding photo", e)
        }
    }

    private fun resizeToLandscape(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val targetHeight = (width * 0.75f).toInt()
        val yOffset = (height - targetHeight) / 2
        return Bitmap.createBitmap(bitmap, 0, yOffset, width, targetHeight)
    }


    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        try {
            val width = bitmap.width
            val height = bitmap.height

            if (width <= maxSize && height <= maxSize) {
                return bitmap
            }

            val ratio: Float = width.toFloat() / height.toFloat()
            val newWidth: Int
            val newHeight: Int

            if (width > height) {
                newWidth = maxSize
                newHeight = (maxSize / ratio).toInt()
            } else {
                newHeight = maxSize
                newWidth = (maxSize * ratio).toInt()
            }

            return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        } catch (e: Exception) {
            Log.e("PhotoViewModel", "Error resizing bitmap", e)
            return bitmap
        }
    }

    fun selectedFrame(painter: Painter) {
        selectedFrame = painter
    }

    fun selectPhoto(index: Int) {
        try {
            if (index < 0 || index >= _capturedPhotos.size) {
                return
            }

            if (_selectedPhotosOrder.contains(index)) {
                _selectedPhotosOrder.remove(index)
            } else if (_selectedPhotosOrder.size < maxSelectablePhotos) {
                _selectedPhotosOrder.add(index)
            } else {
                _selectedPhotosOrder.add(index)
            }

            selectedPhotoIndex = index
        } catch (e: Exception) {
            Log.e("PhotoViewModel", "Error selecting photo", e)
        }
    }

    fun getSelectionOrder(index: Int): Int? {
        if (index < 0 || index >= _capturedPhotos.size) return null
        val position = _selectedPhotosOrder.indexOf(index)
        return if (position != -1) position + 1 else null
    }

    fun clearPhotos() {
        try {
            _capturedPhotos.clear()
            _selectedPhotosOrder.clear()
            selectedPhotoIndex = null
        } catch (e: Exception) {
            Log.e("PhotoViewModel", "Error clearing photos", e)
        }
    }
}

