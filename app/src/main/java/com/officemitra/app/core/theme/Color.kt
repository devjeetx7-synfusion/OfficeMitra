package com.officemitra.app.core.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// V5.0 Design Tokens - Light Theme
val BrandInk = Color(0xFF14232B)
val BrandTeal = Color(0xFF007F78)
val TealDark = Color(0xFF006963)
val TealLight = Color(0xFFD8F3E9)
val WarmCoral = Color(0xFFEF795C)
val WarmSand = Color(0xFFF5E8D5)
val AppCanvas = Color(0xFFF6F8F6)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceMuted = Color(0xFFEEF3F1)
val TextPrimaryLight = Color(0xFF14232B)
val TextSecondaryLight = Color(0xFF62737A)
val BorderLight = Color(0xFFE2EAE7)
val SuccessLight = Color(0xFF078568)
val WarningLight = Color(0xFFA86512)
val ErrorLight = Color(0xFFC23D45)

// V5.0 Design Tokens - Dark Theme
val BackgroundDark = Color(0xFF101A1E)
val SurfaceDark = Color(0xFF19272B)
val ElevatedSurfaceDark = Color(0xFF223338)
val PrimaryTealDark = Color(0xFF58C8B8)
val PrimaryTextDark = Color(0xFFF2F7F5)
val SecondaryTextDark = Color(0xFFB4C6C5)
val BorderDark = Color(0xFF34474B)
val SuccessDark = Color(0xFF34D399)
val WarningDark = Color(0xFFFBBF24)
val ErrorDark = Color(0xFFF87171)

// Backward Compatibility Aliases & Semantic Colors
val PrimaryLight = BrandTeal
val SecondaryLight = TealDark
val PrimaryDark = PrimaryTealDark
val SecondaryDark = PrimaryTealDark
val BackgroundLight = AppCanvas
val TextPrimaryDark = PrimaryTextDark
val TextSecondaryDark = SecondaryTextDark
val SuccessColor = SuccessLight
val WarningColor = WarningLight
val ErrorColor = ErrorLight
val PrimaryDarkColor = TealDark
val SecondaryDarkScheme = SecondaryTextDark
val PrimaryDarkScheme = PrimaryTealDark
val CyanLight = BrandTeal
val PinkLight = WarmCoral

// V5.0 Gradients (Subtle & Purposeful)
val MidnightHeroGradient = Brush.linearGradient(
    colors = listOf(BrandInk, Color(0xFF1C2F3A))
)

val PrimaryGradient = Brush.linearGradient(
    colors = listOf(BrandTeal, TealDark)
)

val AttendanceGradient = Brush.linearGradient(
    colors = listOf(BrandInk, Color(0xFF1E323D))
)

val LeaveGradient = Brush.linearGradient(
    colors = listOf(BrandTeal, Color(0xFF005A55))
)

val SuccessGradient = Brush.linearGradient(
    colors = listOf(SuccessLight, Color(0xFF05654F))
)

val WarmAccentGradient = Brush.linearGradient(
    colors = listOf(WarmCoral, Color(0xFFD96347))
)

// Soft Icon Container Accents
val SoftTealContainer = TealLight
val SoftCoralContainer = Color(0xFFFDECE8)
val SoftSandContainer = WarmSand
val SoftMutedContainer = SurfaceMuted
val SoftSuccessContainer = Color(0xFFE3F6F1)
val SoftWarningContainer = Color(0xFFFEF3E6)

val SoftTealContainerDark = Color(0xFF163C38)
val SoftCoralContainerDark = Color(0xFF42221C)
val SoftSandContainerDark = Color(0xFF3D3425)
val SoftMutedContainerDark = Color(0xFF1E2D32)

// Compatibility Aliases for Soft Icon Containers
val PastelCyan = SoftTealContainer
val PastelCyanDark = SoftTealContainerDark
val PastelPink = SoftCoralContainer
val PastelPinkDark = SoftCoralContainerDark
val PastelViolet = SoftTealContainer
val PastelVioletDark = SoftTealContainerDark
val PastelAmber = SoftSandContainer
val PastelAmberDark = SoftSandContainerDark
val PastelGreen = SoftSuccessContainer
val PastelGreenDark = SoftTealContainerDark
val PastelBlue = SoftTealContainer
val PastelBlueDark = SoftTealContainerDark
val PastelRose = SoftCoralContainer
