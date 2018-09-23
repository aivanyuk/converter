package com.aivanyuk.android.converter

import android.widget.ImageView
import com.squareup.picasso.Picasso

interface ImageLoader {
    fun loadImage(url: String, imageView: ImageView)
    fun loadImage(url: String, size: Pair<Int, Int>, imageView: ImageView)
}

class ImageLoaderImpl: ImageLoader {
    override fun loadImage(url: String, size: Pair<Int, Int>, imageView: ImageView) {
        Picasso.get().load(url).resize(size.first, size.second).into(imageView)
    }

    override fun loadImage(url: String, imageView: ImageView) {
        Picasso.get().load(url).into(imageView)
    }

}