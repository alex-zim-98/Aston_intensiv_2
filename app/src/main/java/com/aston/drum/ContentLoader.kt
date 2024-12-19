package com.aston.drum

import android.graphics.Bitmap

interface ContentLoader {
    suspend fun loadImage(url: String): Bitmap
}