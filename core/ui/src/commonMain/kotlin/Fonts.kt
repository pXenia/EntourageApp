package com.entourageapp.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import entourageapp.core.ui.generated.resources.Lato_Medium
import entourageapp.core.ui.generated.resources.Lato_Regular
import entourageapp.core.ui.generated.resources.Lato_Semibold
import entourageapp.core.ui.generated.resources.Raleway_Bold
import entourageapp.core.ui.generated.resources.Raleway_ExtraBold
import entourageapp.core.ui.generated.resources.Raleway_Medium
import entourageapp.core.ui.generated.resources.Raleway_Regular
import entourageapp.core.ui.generated.resources.Raleway_SemiBold
import entourageapp.core.ui.generated.resources.Res
import org.jetbrains.compose.resources.Font

// Семейство Raleway (для заголовков)
@Composable
fun ralewayFontFamily() = FontFamily(
    Font(Res.font.Raleway_Bold, FontWeight.Bold),
    Font(Res.font.Raleway_Medium, FontWeight.Medium),
    Font(Res.font.Raleway_Regular, FontWeight.Normal),
    Font(Res.font.Raleway_SemiBold, FontWeight.SemiBold),
    Font(Res.font.Raleway_ExtraBold, FontWeight.ExtraBold)
)

// Семейство Lato (для текстов)
@Composable
fun latoFontFamily() = FontFamily(
    Font(Res.font.Lato_Regular, FontWeight.Normal),
    Font(Res.font.Lato_Semibold, FontWeight.SemiBold),
    Font(Res.font.Lato_Medium, FontWeight.Medium),
)