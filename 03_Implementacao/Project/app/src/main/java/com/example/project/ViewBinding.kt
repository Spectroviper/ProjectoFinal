package com.example.project

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.skydoves.progressview.ProgressView

object ViewBinding {
    /**/
    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageResource(imageView: AppCompatImageView, resource: Int) {
        imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, resource))
    }

    /**/
    @JvmStatic
    @BindingAdapter("app:cardBackgroundColorType")
    fun setCardBackgroundColor(carview: CardView, resource: Int) {
        carview.setCardBackgroundColor(ContextCompat.getColor(carview.context, resource))
    }

    /**/
    @JvmStatic
    @BindingAdapter("paletteImage", "paletteCard")
    fun bindLoadImagePalette(
        view: AppCompatImageView, url: String, paletteCard:
        MaterialCardView
    ) {
        Glide.with(view.context)
            .asBitmap()
            .load(url)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("TAG", e?.message.toString())
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    p1: Any?,
                    p2: com.bumptech.glide.request.target.Target<Bitmap>?,
                    p3: DataSource?,
                    p4: Boolean
                ): Boolean {
                    Log.d("TAG", "OnResourceReady")
                    if (resource != null) {
                        val p: Palette = Palette.from(resource).generate()
                        val rgb = p?.lightMutedSwatch?.rgb
                        if (rgb != null) {
                            paletteCard.setCardBackgroundColor(rgb)
                        }
                    }
                    return false
                }
            })
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("paletteImage", "paletteView")
    fun bindLoadImagePaletteView(view: AppCompatImageView, url: String, paletteView: View) {
        val context = view.context
        Glide.with(view.context)
            .asBitmap()
            .load(url)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("TAG", e?.message.toString())
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    p1: Any?,
                    p2: com.bumptech.glide.request.target.Target<Bitmap>?,
                    p3: DataSource?,
                    p4: Boolean
                ): Boolean {
                    Log.d("TAG", "OnResourceReady")
                    if (resource != null) {
                        val p: Palette = Palette.from(resource).generate()
                        val rgb = p?.lightMutedSwatch?.rgb
                        val light = p?.lightVibrantSwatch?.rgb
                        val domain = p?.dominantSwatch?.rgb
                        if (domain != null) {
                            if (light != null) {
                                val gfgGradient = GradientDrawable(
                                    GradientDrawable.Orientation.TOP_BOTTOM,
                                    intArrayOf(
                                        domain,
                                        light
                                    )
                                )
                                paletteView.background = gfgGradient
                                setToolbarColor(context, domain)
                            } else {
                                if (rgb != null) {
                                    paletteView.setBackgroundColor(rgb)
                                    setToolbarColor(context, rgb)
                                }
                            }
                        }
                    }
                    return false
                }
            })
            .into(view)
    }

    fun setToolbarColor(context: Context, color: Int) {
        if (context is AppCompatActivity) {
            context.window.apply {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = color
            }
        }
    }

    @JvmStatic
    @BindingAdapter("paletteImage", "paletteShapeableImageView")
    fun bindLoadImagePaletteShapeableImageView(
        view: AppCompatImageView, url: String,
        paletteShapeableImageView: ShapeableImageView
    ) {
        val context = view.context
        Glide.with(view.context)
            .asBitmap()
            .load(url)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("TAG", e?.message.toString())
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    p1: Any?,
                    p2: com.bumptech.glide.request.target.Target<Bitmap>?,
                    p3: DataSource?,
                    p4: Boolean
                ): Boolean {
                    Log.d("TAG", "OnResourceReady")
                    if (resource != null) {
                        val p: Palette = Palette.from(resource).generate()
                        val rgb = p?.lightMutedSwatch?.rgb
                        val light = p?.lightVibrantSwatch?.rgb
                        val domain = p?.dominantSwatch?.rgb
                        if (domain != null) {
                            if (light != null) {
                                val gfgGradient = GradientDrawable(
                                    GradientDrawable.Orientation.TOP_BOTTOM,
                                    intArrayOf(
                                        domain,
                                        light
                                    )
                                )
                                paletteShapeableImageView.background = gfgGradient
                            } else {
                                if (rgb != null) {
                                    paletteShapeableImageView.setBackgroundColor(rgb)
                                }
                            }
                        }
                    }
                    return false
                }
            })
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("progressView_progress")
    fun bindProgressViewProgress(progressView: ProgressView, value: Int?) {
        if (value != null) {
            progressView.progress = value.toFloat()
        }
    }

    @JvmStatic
    @BindingAdapter("progressView_max")
    fun bindProgressViewMax(progressView: ProgressView, value: Int?) {
        if (value != null) {
            progressView.max = value.toFloat()
        }
    }

    @JvmStatic
    @BindingAdapter("progressView_labelText")
    fun bindProgressViewLabelText(progressView: ProgressView, value: Int) {
        progressView.labelText = value.toString()
    }

    @JvmStatic
    @BindingAdapter("onBackPressed")
    fun bindOnBackPressed(view: View, onBackPress: Boolean) {
        val context = view.context
        if (onBackPress && context is OnBackPressedDispatcherOwner) {
            view.setOnClickListener {
                context.onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("app:backgroundTint")
    fun bindBackgroundColor(view: MaterialButton, resource: Int) {
        view.background.setTint(ContextCompat.getColor(view.context, resource))
    }
}



