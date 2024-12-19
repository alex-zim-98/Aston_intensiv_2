package com.aston.drum

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RainbowDrumViewModel : ViewModel() {
    private val contentLoader = NetworkContentLoader()

    private val _isLoading = MutableLiveData(false)
    val isLoading get() = _isLoading

    private val _currentContent = MutableLiveData<Any>()
    val currentContent: LiveData<Any> get() = _currentContent

    fun onColorSelected(color: Int) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val content = when (color) {
                Color.RED, Color.YELLOW, Color.parseColor(RainbowDrumView.COLOR_VIOLET),
                Color.parseColor(RainbowDrumView.COLOR_INDIGO)
                    -> "Text: color - $color"
                Color.parseColor(RainbowDrumView.COLOR_ORANGE), Color.GREEN, Color.BLUE
                    -> contentLoader.loadImage(URL)
                else -> throw RuntimeException("Unexpected color!")
            }
            _currentContent.postValue(content)
            _isLoading.postValue(false)
        }
    }

    fun resetContent() {
        _currentContent.value = ""
    }

    companion object {
        private const val URL = "https://placebeard.it/1280x720"
    }
}
