package com.example.lirogram

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan
import androidx.annotation.AnimatorRes
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
class LottieSpan(private val context: Context, private val assetFileName: String) : ReplacementSpan() {

    private var drawable: LottieDrawable? = null

    init {
        val result = LottieCompositionFactory.fromAssetSync(context, assetFileName)
        val composition = result.value

        drawable = LottieDrawable().apply {
            setComposition(composition)
            playAnimation()
            repeatCount = LottieDrawable.INFINITE
        }
    }

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return drawable?.intrinsicWidth ?: 0
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        drawable?.let {
            val transY = y + paint.fontMetricsInt.ascent
            canvas.save()
            canvas.translate(x, transY.toFloat())
            it.setBounds(0, 0, 80,80)
            it.draw(canvas)
            canvas.restore()
        }
    }
}