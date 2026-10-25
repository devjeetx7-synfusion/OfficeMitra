package com.officemitra.app.core.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val SmallShape = RoundedCornerShape(8.dp)
val MediumShape = RoundedCornerShape(12.dp)
val LargeShape = RoundedCornerShape(16.dp)
val ExtraLargeShape = RoundedCornerShape(22.dp)
val HeroShape = RoundedCornerShape(24.dp)
val FloatingDockShape = CircleShape

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = SmallShape,
    medium = MediumShape,
    large = LargeShape,
    extraLarge = ExtraLargeShape
)
