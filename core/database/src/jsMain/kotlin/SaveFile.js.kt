package com.entourageapp.core.database

import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

actual fun saveAndOpenFile(fileName: String, bytes: ByteArray) {
    val blob = Blob(
        arrayOf(bytes.toTypedArray()),
        BlobPropertyBag("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    )
    val url = URL.createObjectURL(blob)
    val a = document.createElement("a") as HTMLAnchorElement
    a.href = url
    a.download = fileName
    document.body?.appendChild(a)
    a.click()
    document.body?.removeChild(a)
    URL.revokeObjectURL(url)
}