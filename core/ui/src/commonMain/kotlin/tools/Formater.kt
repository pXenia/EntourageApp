package com.entourageapp.core.ui.tools

fun formatYears(startDate: String, endDate: String?): String {
    val startYear = startDate.take(4)
    val endYear = endDate?.take(4)

    return when {
        endYear == null || endYear == startYear -> startYear
        else -> "$startYear–$endYear"
    }
}