package com.snapshot.feature.screen.photo.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class PhotoViewModel : ViewModel() {
    private val _capturedPhotos = mutableStateListOf<Bitmap>()
    val capturedPhotos: List<Bitmap> = _capturedPhotos

    var selectedPhotoIndex by mutableStateOf<Int?>(null)
        private set

    private val _selectedPhotosOrder = mutableStateListOf<Int>()
    val selectedPhotosOrder: List<Int> = _selectedPhotosOrder

    private val maxSelectablePhotos = 4

    private val _selectionMessage = MutableStateFlow<String?>(null)
    val selectionMessage: StateFlow<String?> = _selectionMessage

    fun clearSelectionMessage() {
        _selectionMessage.value = null
    }

    fun addPhoto(bitmap: Bitmap) {
        try {
            if (_capturedPhotos.size < 8) {
                val resizedBitmap = resizeBitmap(bitmap, 800)
                _capturedPhotos.add(resizedBitmap)
            }
        } catch (e: Exception) {
            Log.e("PhotoViewModel", "Error adding photo", e)
            _selectionMessage.value = "사진 추가 중 오류가 발생했습니다"
        }
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

    fun selectPhoto(index: Int) {
        try {
            if (index < 0 || index >= _capturedPhotos.size) {
                _selectionMessage.value = "유효하지 않은 사진입니다"
                return
            }

            if (_selectedPhotosOrder.contains(index)) {
                _selectedPhotosOrder.remove(index)
                _selectionMessage.value = "사진 선택이 취소되었습니다"
            } else if (_selectedPhotosOrder.size < maxSelectablePhotos) {
                _selectedPhotosOrder.add(index)
            } else {
                val lastSelected = _selectedPhotosOrder.removeAt(maxSelectablePhotos - 1)
                _selectedPhotosOrder.add(index)
                _selectionMessage.value = "최대 4개까지 선택 가능합니다. ${getSelectionOrder(lastSelected)}번 사진이 대체되었습니다."
            }

            selectedPhotoIndex = index
        } catch (e: Exception) {
            Log.e("PhotoViewModel", "Error selecting photo", e)
            _selectionMessage.value = "사진 선택 중 오류가 발생했습니다"
        }
    }

    fun getSelectionOrder(index: Int): Int? {
        if (index < 0 || index >= _capturedPhotos.size) return null
        val position = _selectedPhotosOrder.indexOf(index)
        return if (position != -1) position + 1 else null
    }

    fun getPhotoIndexAtPosition(position: Int): Int? {
        return if (position >= 0 && position < _selectedPhotosOrder.size) {
            _selectedPhotosOrder[position]
        } else {
            null
        }
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

