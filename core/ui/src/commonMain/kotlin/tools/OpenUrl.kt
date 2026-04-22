package com.entourageapp.core.ui.tools

fun formatUrl(url: String): String {
    if (url.isBlank()) return ""
    return if (!url.startsWith("http://") && !url.startsWith("https://")) {
        "https://$url"
    } else {
        url
    }
}