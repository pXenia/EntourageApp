package com.entourageapp.core.database

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.download

actual suspend fun saveFile(fileName: String, bytes: ByteArray) {
    FileKit.download(
        bytes = bytes,
        fileName = fileName
    )
}