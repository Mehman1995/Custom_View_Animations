package ru.netology.customview.ui

import android.content.AttributionSource
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.toXfermode
import ru.netology.customview.R
import ru.netology.customview.utils.AndroidUtils
import kotlin.math.min
import kotlin.random.Random

class StatsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    divStyleAttr: Int = 0,
    divStyleRes: Int = 0
) : View(context, attributeSet, divStyleAttr, divStyleRes) {

    var data: List<Float> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        strokeWidth = AndroidUtils.dp(context, 5).toFloat()
    }


    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = AndroidUtils.dp(context, 24).toFloat()
        textAlign = Paint.Align.CENTER
    }

    private var center = PointF()
    private var radius = 0F
    private var oval = RectF()
    private var colors = emptyList<Int>()

    init {
        context.withStyledAttributes(attributeSet, R.styleable.StatsView) {
            textPaint.textSize = getDimension(R.styleable.StatsView_textSize, textPaint.textSize)
            paint.strokeWidth = getDimension(R.styleable.StatsView_lineWidth, paint.strokeWidth)
            colors = listOf(
                getColor(R.styleable.StatsView_color1, generateRandomColor()),
                getColor(R.styleable.StatsView_color2, generateRandomColor()),
                getColor(R.styleable.StatsView_color3, generateRandomColor()),
                getColor(R.styleable.StatsView_color4, generateRandomColor())


            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - paint.strokeWidth
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
    }

    override fun onDraw(canvas: Canvas?) {
        if (data.isEmpty()) {
            return
        }

        var startAngle = -90F
        data.forEachIndexed { index, datum ->
            val angle = (datum / (data.maxOrNull()?.times(data.count())!!)) * 360
            paint.color = colors.getOrElse(index) { generateRandomColor() }
            canvas?.drawArc(oval, startAngle, angle, false, paint)
            startAngle += angle
        }

        val text = (data.sum() / (data.maxOrNull()?.times(data.count())!!)) * 100
        canvas?.drawText(
            "%.2f%%".format(text),
            center.x,
            center.y + textPaint.textSize / 4F,
            textPaint
        )

        if (text == 100F) {
            paint.color = colors[0]
            canvas?.drawArc(oval, startAngle, 1F, false, paint)
        }

    }

    private fun generateRandomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())


}