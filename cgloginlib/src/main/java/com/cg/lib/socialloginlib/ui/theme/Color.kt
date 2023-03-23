package com.cg.lib.socialloginlib.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

//custom color can be used in the same package
val lightGray = Color(0x76D3D3D3)
val lightGreen200 = Color(0x9932CD32)

// Custom color that can be used in the ui class
val Colors.lightGreen: Color
    @Composable
    get() = lightGreen200