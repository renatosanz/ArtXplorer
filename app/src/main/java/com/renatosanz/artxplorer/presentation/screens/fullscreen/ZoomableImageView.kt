package com.renatosanz.artxplorer.presentation.screens.fullscreen

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min

// Minimal pinch/drag/double-tap viewer, replacing the unmaintained PhotoView.
class ZoomableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val matrix = Matrix()
    private val lastTouch = FloatArray(2)
    private var scale = 1f

    private val scaleDetector = ScaleGestureDetector(
        context,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                zoomTo(scale * detector.scaleFactor, detector.focusX, detector.focusY)
                return true
            }
        }
    )

    private val gestureDetector = GestureDetector(
        context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent) = true

            override fun onDoubleTap(e: MotionEvent): Boolean {
                zoomTo(if (scale > 1f) MIN_SCALE else DOUBLE_TAP_SCALE, e.x, e.y)
                return true
            }
        }
    )

    init {
        scaleType = ScaleType.MATRIX
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        fitToView()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        fitToView()
    }

    private fun fitToView() {
        val drawable = drawable ?: return
        val imageWidth = drawable.intrinsicWidth
        val imageHeight = drawable.intrinsicHeight
        if (imageWidth <= 0 || imageHeight <= 0 || width == 0 || height == 0) return
        val fit = min(width.toFloat() / imageWidth, height.toFloat() / imageHeight)
        matrix.reset()
        matrix.postScale(fit, fit)
        matrix.postTranslate(
            (width - imageWidth * fit) / 2f,
            (height - imageHeight * fit) / 2f
        )
        scale = MIN_SCALE
        imageMatrix = matrix
    }

    private fun zoomTo(target: Float, focusX: Float, focusY: Float) {
        val clamped = target.coerceIn(MIN_SCALE, MAX_SCALE)
        val relative = clamped / scale
        scale = clamped
        matrix.postScale(relative, relative, focusX, focusY)
        clampToView()
    }

    private fun pan(dx: Float, dy: Float) {
        matrix.postTranslate(dx, dy)
        clampToView()
    }

    // Keep the image centered when smaller than the view, otherwise keep its edges outside it.
    private fun clampToView() {
        val drawable = drawable ?: return
        val bounds = RectF(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
        matrix.mapRect(bounds)
        var dx = 0f
        var dy = 0f
        if (bounds.width() <= width) dx = (width - bounds.width()) / 2f - bounds.left
        else if (bounds.left > 0f) dx = -bounds.left
        else if (bounds.right < width) dx = width - bounds.right
        if (bounds.height() <= height) dy = (height - bounds.height()) / 2f - bounds.top
        else if (bounds.top > 0f) dy = -bounds.top
        else if (bounds.bottom < height) dy = height - bounds.bottom
        matrix.postTranslate(dx, dy)
        imageMatrix = matrix
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                lastTouch[0] = event.x
                lastTouch[1] = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                if (!scaleDetector.isInProgress && event.pointerCount == 1 && scale > 1f) {
                    pan(event.x - lastTouch[0], event.y - lastTouch[1])
                    lastTouch[0] = event.x
                    lastTouch[1] = event.y
                }
            }
        }
        return true
    }

    override fun performClick(): Boolean = super.performClick()

    private companion object {
        const val MIN_SCALE = 1f
        const val MAX_SCALE = 5f
        const val DOUBLE_TAP_SCALE = 2.5f
    }
}
