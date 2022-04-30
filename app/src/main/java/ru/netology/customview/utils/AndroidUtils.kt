package ru.netology.customview.utils

import android.content.Context
import androidx.annotation.Px
import kotlin.math.ceil

object AndroidUtils {

    @Px
    fun dp(context: Context, dp: Int): Int =
        ceil(context.resources.displayMetrics.density * dp).toInt()

}