package com.apicela.training.ui.utils

import android.content.Context
import android.widget.ImageView
import com.apicela.training.utils.GifDrawableImageViewTarget
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ImageHelper {
    companion object {

        fun setImage(
            context: Context,
            view: ImageView,
            image: String,
            isGif: Boolean
        ) {
            val resourceId = context.resources.getIdentifier(image, "drawable", context.packageName)
            // recurso encontrado
            if (resourceId != 0) {
                Glide.with(context)
                    .asBitmap()
                    .load(resourceId)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Desativa cache de disco para depuração
                    .into(view)
                if (isGif) {
                    view.setOnClickListener {
                        Glide.with(context)
                            .load(resourceId)
                            .into(GifDrawableImageViewTarget(view, 1))
                    }
                }
            } else {
                Glide.with(context)
                    .asBitmap()
                    .load(image)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Desativa cache de disco para depuração
                    .into(view)
                if (isGif) {

                    view.setOnClickListener {
                        Glide.with(context)
                            .load(image)
                            .into(GifDrawableImageViewTarget(view, 1))
                    }
                }
            }
        }
    }
}