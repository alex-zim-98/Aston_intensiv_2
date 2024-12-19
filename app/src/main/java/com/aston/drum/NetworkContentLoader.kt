package com.aston.drum

import android.graphics.Bitmap
import com.squareup.picasso.Picasso

class NetworkContentLoader : ContentLoader {
    override suspend fun loadImage(url: String): Bitmap {
        return Picasso.get().load(url).get()
    }
}