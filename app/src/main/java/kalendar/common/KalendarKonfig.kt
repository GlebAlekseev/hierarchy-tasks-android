package com.himanshoe.kalendar.common

import androidx.annotation.IntRange
import java.util.Locale

/**
 * [KalendarKonfig] represents the config needed for rendering calendar
 * @param[yearRange] gives the min/max year range
 * @param[weekCharacters] helps you set the number of character in Week name, default is 3
 * @param[locale] helps you set the locale where default is [Locale.ENGLISH]
 */
data class KalendarKonfig(
    val yearRange: YearRange = YearRange(),
    @IntRange(from = 1, to = 4)
    val weekCharacters: Int = 3,
    val locale: Locale = Locale.ENGLISH,
)

/**
 * [YearRange] represents range from
 * [min] years to
 * [max] years
 */
data class YearRange(val min: Int = 0, val max: Int = 0)
