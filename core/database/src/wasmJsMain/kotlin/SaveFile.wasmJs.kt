package com.entourageapp.core.database

actual fun saveAndOpenFile(fileName: String, bytes: ByteArray) {
    saveFileWasm(fileName, bytes)
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("""
(fileName, bytes) => {
    const blob = new Blob([bytes], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
}
""")
external fun saveFileWasm(fileName: String, bytes: ByteArray)