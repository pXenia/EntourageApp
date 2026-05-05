package com.entourageapp.core.ui.tools

import kotlinx.datetime.LocalDate

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

fun formatAmountWithCurrency(amount: Float): String {
    val formatted = amount.toInt().toString()
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()
    return "$formatted ₽"
}

fun tryParseDate(uiDate: String): LocalDate? {
    if (uiDate.length != 8) return null
    return try {
        val day = uiDate.substring(0, 2).toInt()
        val month = uiDate.substring(2, 4).toInt()
        val year = uiDate.substring(4, 8).toInt()
        LocalDate(year, month, day)
    } catch (e: Exception) {
        null
    }
}

fun formatDate(dateString: String): String {
    return try {
        val date = LocalDate.parse(dateString)
        "${date.dayOfMonth.toString().padStart(2, '0')}.${date.monthNumber.toString().padStart(2, '0')}.${date.year}"
    } catch (e: Exception) {
        dateString
    }
}

fun getPlural(n: Int, one: String, two: String, five: String): String {
    val n1 = n % 10
    val n10 = n % 100
    return when {
        n1 == 1 && n10 != 11 -> one
        n1 in 2..4 && n10 !in 12..14 -> two
        else -> five
    }
}