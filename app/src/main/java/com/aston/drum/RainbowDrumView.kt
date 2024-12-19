package com.aston.drum

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd

class RainbowDrumView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var colors = listOf(
        Color.RED,
        Color.parseColor(COLOR_ORANGE),
        Color.YELLOW,
        Color.GREEN,
        Color.parseColor(COLOR_INDIGO),
        Color.BLUE,
        Color.parseColor(COLOR_VIOLET),
    )

    private var selectedColor: Int = colors[0]
    private var radius: Float = CIRCLE_RADIUS.pix2DP()
    private var isSpinning = false

    var angle = 0f

    var onColorSelected: ((Int) -> Unit)? = null

    init {
        isClickable = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val segmentAngle = 360f / colors.size

        colors.forEachIndexed { index, color ->
            paint.color = color
            canvas.drawArc(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius,
                segmentAngle * index + angle,
                segmentAngle,
                true,
                paint
            )
        }
    }

    fun spinAndStop() {
        if (isSpinning) return

        isSpinning = true
        val randomIndex = (colors.indices).random()
        val stopAngle = randomIndex * (360f / colors.size)

        ValueAnimator.ofFloat(0f, 3600f + stopAngle).apply {
            duration = 3000
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                angle = animation.animatedValue as Float
                rotateToAngle(angle % 360)
                invalidate()
            }
            doOnEnd {
                isSpinning = false
                selectedColor = colors[randomIndex]
                onColorSelected?.invoke(selectedColor)
            }
            start()
        }
    }

    private fun rotateToAngle(angle: Float) {
        val segmentAngle = 360f / colors.size
        val segmentIndex = ((angle / segmentAngle).toInt()) % colors.size
        selectedColor = colors[segmentIndex]
    }

    fun adjustSize(scaleFactor: Float) {
        radius = (scaleFactor * CIRCLE_RADIUS).pix2DP()
        invalidate()
    }

    private fun Float.pix2DP(): Float {
        return this / Resources.getSystem().displayMetrics.density
    }

    companion object {
        private const val CIRCLE_RADIUS = 300f

        const val COLOR_ORANGE = "#FFA500"
        const val COLOR_INDIGO = "#4b0082"
        const val COLOR_VIOLET = "#7F00FF"
    }
}