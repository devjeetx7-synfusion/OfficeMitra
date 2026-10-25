package com.officemitra.app.core.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Centralized rounded-outline icon mappings for Office Mitra.
 * All icon styles follow a consistent Lucide-inspired outlined visual system.
 */
object AppIcons {
    // Main Navigation
    val Home: ImageVector = Icons.Outlined.Home
    val Attendance: ImageVector = Icons.Outlined.AccessTime
    val Leave: ImageVector = Icons.AutoMirrored.Outlined.EventNote
    val Profile: ImageVector = Icons.Outlined.Person
    val Person: ImageVector = Icons.Outlined.Person

    // Core Feature Modules
    val Salary: ImageVector = Icons.AutoMirrored.Outlined.ReceiptLong
    val Holiday: ImageVector = Icons.Outlined.BeachAccess
    val Approvals: ImageVector = Icons.Outlined.HowToReg
    val History: ImageVector = Icons.Outlined.History
    val Notifications: ImageVector = Icons.Outlined.Notifications
    val Calendar: ImageVector = Icons.Outlined.CalendarToday
    val DateRange: ImageVector = Icons.Outlined.DateRange
    val Dashboard: ImageVector = Icons.Outlined.Dashboard
    val CalendarMonth: ImageVector = Icons.Outlined.CalendarMonth

    // Actions & Operations
    val PunchIn: ImageVector = Icons.AutoMirrored.Outlined.Login
    val PunchOut: ImageVector = Icons.AutoMirrored.Outlined.Logout
    val Location: ImageVector = Icons.Outlined.LocationOn
    val Camera: ImageVector = Icons.Outlined.CameraAlt
    val CheckCircle: ImageVector = Icons.Outlined.CheckCircle
    val Cancel: ImageVector = Icons.Outlined.Cancel
    val Edit: ImageVector = Icons.Outlined.Edit
    val Add: ImageVector = Icons.Outlined.Add
    val Download: ImageVector = Icons.Outlined.Download
    val View: ImageVector = Icons.Outlined.Visibility
    val ViewOff: ImageVector = Icons.Outlined.VisibilityOff
    val Pdf: ImageVector = Icons.Outlined.PictureAsPdf
    val DoneAll: ImageVector = Icons.Outlined.DoneAll
    val ArrowBack: ImageVector = Icons.AutoMirrored.Outlined.ArrowBack
    val ChevronRight: ImageVector = Icons.Outlined.ChevronRight

    // Auth & Company & Account
    val Business: ImageVector = Icons.Outlined.Business
    val Email: ImageVector = Icons.Outlined.Email
    val Phone: ImageVector = Icons.Outlined.Phone
    val Lock: ImageVector = Icons.Outlined.Lock
    val Settings: ImageVector = Icons.Outlined.Settings
    val Info: ImageVector = Icons.Outlined.Info
    val Badge: ImageVector = Icons.Outlined.Badge
    val QrCode: ImageVector = Icons.Outlined.QrCode
    val EventAvailable: ImageVector = Icons.Outlined.EventAvailable
    val Event: ImageVector = Icons.Outlined.Event

    // Empty, Error, System States
    val Inbox: ImageVector = Icons.Outlined.Inbox
    val Error: ImageVector = Icons.Outlined.ErrorOutline
    val WifiOff: ImageVector = Icons.Outlined.WifiOff
    val Timer: ImageVector = Icons.Outlined.Timer
}
