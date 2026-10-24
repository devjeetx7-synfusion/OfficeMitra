package com.officemitra.app.core.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Light Theme
val PrimaryLight = Color(0xFF635BFF)
val SecondaryLight = Color(0xFF2563EB)
val CyanLight = Color(0xFF06B6D4)
val PinkLight = Color(0xFFEC4899)
val BackgroundLight = Color(0xFFF7F8FC)
val SurfaceLight = Color(0xFFFFFFFF)
val TextPrimaryLight = Color(0xFF111827)
val TextSecondaryLight = Color(0xFF6B7280)
val BorderLight = Color(0xFFE5E7EB)
val SuccessLight = Color(0xFF10B981)
val WarningLight = Color(0xFFD97706)
val ErrorLight = Color(0xFFDC2626)

// Aliases for compatibility
val SuccessColor = SuccessLight
val WarningColor = WarningLight
val ErrorColor = ErrorLight
val PrimaryDarkColor = Color(0xFF1D4ED8)
val SecondaryDarkScheme = Color(0xFF94A3B8)
val PrimaryDarkScheme = Color(0xFF3B82F6)

// Dark Theme
val BackgroundDark = Color(0xFF0B1020)
val SurfaceDark = Color(0xFF172033)
val ElevatedSurfaceDark = Color(0xFF202C43)
val PrimaryDark = Color(0xFFA5B4FC)
val SecondaryDark = Color(0xFF60A5FA)
val TextPrimaryDark = Color(0xFFF9FAFB)
val TextSecondaryDark = Color(0xFFCBD5E1)
val BorderDark = Color(0xFF334155)
val SuccessDark = Color(0xFF34D399)
val WarningDark = Color(0xFFFBBF24)
val ErrorDark = Color(0xFFF87171)

// Gradients
val PrimaryGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF635BFF), Color(0xFF2563EB))
)

val AttendanceGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF06B6D4), Color(0xFF2563EB))
)

val LeaveGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFEC4899), Color(0xFF8B5CF6))
)

val SuccessGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF10B981), Color(0xFF059669))
)

// Pastel Icon Containers
val PastelBlue = Color(0xFFE0F2FE)
val PastelViolet = Color(0xFFEDE9FE)
val PastelPink = Color(0xFFFCE7F3)
val PastelCyan = Color(0xFFCFFAFE)
val PastelGreen = Color(0xFFD1FAE5)
val PastelAmber = Color(0xFFFEF3C7)
val PastelRose = Color(0xFFFFE4E6)

val PastelBlueDark = Color(0xFF1E3A8A).copy(alpha = 0.4f)
val PastelVioletDark = Color(0xFF4C1D95).copy(alpha = 0.4f)
val PastelPinkDark = Color(0xFF831843).copy(alpha = 0.4f)
val PastelCyanDark = Color(0xFF164E63).copy(alpha = 0.4f)
val PastelGreenDark = Color(0xFF064E3B).copy(alpha = 0.4f)
val PastelAmberDark = Color(0xFF78350F).copy(alpha = 0.4f)
