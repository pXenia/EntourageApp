package com.entourageapp.core.ui.tools

fun formatYears(startDate: String, endDate: String?): String {
    val startYear = startDate.take(4)
    val endYear = endDate?.take(4)

    return when {
        endYear == null || endYear == startYear -> startYear
        else -> "$startYear–$endYear"
    }
}

fun Double.formatTwoDecimals(): String {
    val rounded = (kotlin.math.round(this * 100) / 100.0).toString()

    val parts = rounded.split(".")
    val integerPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1] else "0"

    val formattedInteger = integerPart
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()

    val formattedDecimal = decimalPart.padEnd(2, '0').take(2)

    return "$formattedInteger.$formattedDecimal"
}

fun formatAmountWithCurrency(amount: Double): String {
    val formatted = amount.toInt().toString()
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()
    return "$formatted ₽"
}