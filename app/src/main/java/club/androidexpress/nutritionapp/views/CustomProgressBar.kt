package club.androidexpress.nutritionapp.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import club.androidexpress.nutritionapp.R

class CustomProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, style: Int = 0) : View(context, attrs, style) {


    private val backgroundArc = RectF()
    private val bgPaint = Paint()
    private val stroke = 40.0f

    private val progressPaint = Paint()
    private var progressValue = 25
    private val bounds = Rect()
    private val barArc = RectF()
    private var progressBarBgColor: Int
    private var progressBarColor: Int

    private val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar)
    private val metrics: DisplayMetrics = resources.displayMetrics
    private val strokeUnitToDp: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, stroke, metrics)

    init {
        progressValue = typedArray.getInt(R.styleable.CustomProgressBar_my_progress, 0)
        progressBarColor = typedArray.getInt(R.styleable.CustomProgressBar_my_progress_color, 0)
        progressBarBgColor = typedArray.getInt(R.styleable.CustomProgressBar_my_progress_bg_color, 0)

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        createArc(canvas)
        createArcProgress(canvas)

    }

    fun setValue(progressValue: Int) {
        this.progressValue = progressValue
        invalidate()
    }

    private fun createArc(canvas: Canvas?) {
        backgroundArc.set(strokeUnitToDp, strokeUnitToDp, width.toFloat() - strokeUnitToDp, height.toFloat() - strokeUnitToDp)

        bgPaint.apply {
            color = progressBarBgColor
            style = Paint.Style.STROKE
            strokeWidth = 60.0f
            isAntiAlias = true
        }

        canvas?.drawArc(backgroundArc, 0.0f, 360.0f, false, bgPaint)
    }

    private fun createArcProgress(canvas: Canvas?) {
        val progress: Float = (360.0f / 100) * progressValue

        canvas?.getClipBounds(bounds)
        barArc.set(strokeUnitToDp, strokeUnitToDp, bounds.right - strokeUnitToDp, bounds.bottom - strokeUnitToDp)

        progressPaint.apply {
            color = progressBarColor
            style = Paint.Style.STROKE
            strokeWidth = strokeUnitToDp - 40.0f
            strokeCap = Paint.Cap.ROUND
            isDither = true
            isAntiAlias = true

        }

        canvas?.drawArc(barArc, 270.0f, progress, false, progressPaint)
    }
}