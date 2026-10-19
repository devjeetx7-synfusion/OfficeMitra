package com.officemitra.app.feature.attendance;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0015\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BG\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\t\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\tH\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\tH\u00c6\u0003JM\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\tH\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\u00032\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u001fH\u00d6\u0001J\t\u0010 \u001a\u00020\tH\u00d6\u0001R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0011R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006!"}, d2 = {"Lcom/officemitra/app/feature/attendance/AttendanceUiState;", "", "isPunchedIn", "", "todayAttendance", "Lcom/officemitra/app/data/model/AttendanceRecord;", "historyRecords", "", "locationStatus", "", "isCameraCaptured", "capturedPhotoPath", "(ZLcom/officemitra/app/data/model/AttendanceRecord;Ljava/util/List;Ljava/lang/String;ZLjava/lang/String;)V", "getCapturedPhotoPath", "()Ljava/lang/String;", "getHistoryRecords", "()Ljava/util/List;", "()Z", "getLocationStatus", "getTodayAttendance", "()Lcom/officemitra/app/data/model/AttendanceRecord;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class AttendanceUiState {
    private final boolean isPunchedIn = false;
    @org.jetbrains.annotations.NotNull()
    private final com.officemitra.app.data.model.AttendanceRecord todayAttendance = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.officemitra.app.data.model.AttendanceRecord> historyRecords = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String locationStatus = null;
    private final boolean isCameraCaptured = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String capturedPhotoPath = null;

    public AttendanceUiState(boolean isPunchedIn, @org.jetbrains.annotations.NotNull()
    com.officemitra.app.data.model.AttendanceRecord todayAttendance, @org.jetbrains.annotations.NotNull()
    java.util.List<com.officemitra.app.data.model.AttendanceRecord> historyRecords, @org.jetbrains.annotations.NotNull()
    java.lang.String locationStatus, boolean isCameraCaptured, @org.jetbrains.annotations.Nullable()
    java.lang.String capturedPhotoPath) {
        super();
    }

    public final boolean isPunchedIn() {
        return false;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.officemitra.app.data.model.AttendanceRecord getTodayAttendance() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.officemitra.app.data.model.AttendanceRecord> getHistoryRecords() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLocationStatus() {
        return null;
    }

    public final boolean isCameraCaptured() {
        return false;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCapturedPhotoPath() {
        return null;
    }

    public final boolean component1() {
        return false;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.officemitra.app.data.model.AttendanceRecord component2() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.officemitra.app.data.model.AttendanceRecord> component3() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }

    public final boolean component5() {
        return false;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.officemitra.app.feature.attendance.AttendanceUiState copy(boolean isPunchedIn, @org.jetbrains.annotations.NotNull()
    com.officemitra.app.data.model.AttendanceRecord todayAttendance, @org.jetbrains.annotations.NotNull()
    java.util.List<com.officemitra.app.data.model.AttendanceRecord> historyRecords, @org.jetbrains.annotations.NotNull()
    java.lang.String locationStatus, boolean isCameraCaptured, @org.jetbrains.annotations.Nullable()
    java.lang.String capturedPhotoPath) {
        return null;
    }

    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }

    @java.lang.Override()
    public int hashCode() {
        return 0;
    }

    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}