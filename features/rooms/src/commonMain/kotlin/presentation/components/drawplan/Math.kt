package com.entourageapp.features.rooms.presentation.components.drawplan

import androidx.compose.ui.geometry.Offset
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

// расстояние между двумя точками
fun Offset.dst(o: Offset) = sqrt((x - o.x).pow(2) + (y - o.y).pow(2))

// привязка к узлам сетки с учетом оффсетов центрирования
fun snapToGrid(offset: Offset, cellSize: Float, offX: Float, offY: Float): Offset = Offset(
    x = ((offset.x - offX) / cellSize).roundToInt() * cellSize + offX,
    y = ((offset.y - offY) / cellSize).roundToInt() * cellSize + offY
)

// расчет площади в кв.м по формуле Гаусса
fun polygonAreaM2(pts: List<Offset>, cellSize: Float, scale: Float = 0.5f): Float {
    if (pts.size < 3) return 0f
    var sum = 0.0
    for (i in pts.indices) {
        val j = (i + 1) % pts.size
        sum += pts[i].x.toDouble() * pts[j].y.toDouble()
        sum -= pts[j].x.toDouble() * pts[i].y.toDouble()
    }
    // перевод пиксельной площади в метрическую
    val mPerPx = scale / cellSize
    return (abs(sum) / 2.0 * mPerPx * mPerPx).toFloat()
}

// расчет длины стены в метрах
fun wallLenM(a: Offset, b: Offset, cellSize: Float, scale: Float = 0.5f) = a.dst(b) / cellSize * scale

// форматирование числа
fun Float.fmt(dec: Int = 2): String {
    val factor = 10f.pow(dec).toInt()
    val r = (this * factor).roundToInt()
    val ip = r / factor
    val fp = abs(r % factor).toString().padStart(dec, '0')
    return "$ip.$fp"
}