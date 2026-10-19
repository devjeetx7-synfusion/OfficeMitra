package com.officemitra.app.core.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000,\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u000f\u001aP\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u00032\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u0007\u001a:\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u0007\u001aT\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0010\b\u0002\u0010\u001a\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0007\u001aH\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u000b2\u0006\u0010\u001f\u001a\u00020\u00032\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0007\u001aF\u0010!\u001a\u00020\u00012\u0006\u0010\"\u001a\u00020\u00032\u0006\u0010#\u001a\u00020\u00032\u0006\u0010$\u001a\u00020\u00032\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0007\u00a8\u0006\'"}, d2 = {"AttendanceCard", "", "date", "", "status", "punchIn", "punchOut", "workingDuration", "onPunchClick", "Lkotlin/Function0;", "isPunchedIn", "", "modifier", "Landroidx/compose/ui/Modifier;", "HolidayCard", "title", "dayOfWeek", "description", "isUpcoming", "LeaveRequestCard", "leaveType", "startDate", "endDate", "daysCount", "", "reason", "onCancelClick", "NotificationCard", "message", "timestamp", "isRead", "category", "onClick", "SalarySlipCard", "monthYear", "period", "publishDate", "onViewPdf", "onDownloadPdf", "app_release"})
public final class ModuleCardsKt {

    @androidx.compose.runtime.Composable()
    public static final void AttendanceCard(@org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    java.lang.String status, @org.jetbrains.annotations.NotNull()
    java.lang.String punchIn, @org.jetbrains.annotations.NotNull()
    java.lang.String punchOut, @org.jetbrains.annotations.NotNull()
    java.lang.String workingDuration, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onPunchClick, boolean isPunchedIn, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }

    @androidx.compose.runtime.Composable()
    public static final void LeaveRequestCard(@org.jetbrains.annotations.NotNull()
    java.lang.String leaveType, @org.jetbrains.annotations.NotNull()
    java.lang.String startDate, @org.jetbrains.annotations.NotNull()
    java.lang.String endDate, int daysCount, @org.jetbrains.annotations.NotNull()
    java.lang.String reason, @org.jetbrains.annotations.NotNull()
    java.lang.String status, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCancelClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }

    @androidx.compose.runtime.Composable()
    public static final void SalarySlipCard(@org.jetbrains.annotations.NotNull()
    java.lang.String monthYear, @org.jetbrains.annotations.NotNull()
    java.lang.String period, @org.jetbrains.annotations.NotNull()
    java.lang.String publishDate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onViewPdf, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDownloadPdf, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }

    @androidx.compose.runtime.Composable()
    public static final void HolidayCard(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    java.lang.String dayOfWeek, @org.jetbrains.annotations.NotNull()
    java.lang.String description, boolean isUpcoming, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }

    @androidx.compose.runtime.Composable()
    public static final void NotificationCard(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    java.lang.String timestamp, boolean isRead, @org.jetbrains.annotations.NotNull()
    java.lang.String category, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}