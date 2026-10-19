package com.officemitra.app.feature.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00c6\u0003JK\u0010\u001e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00052\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00c6\u0001J\u0013\u0010\u001f\u001a\u00020\u00052\b\u0010 \u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010!\u001a\u00020\tH\u00d6\u0001J\t\u0010\"\u001a\u00020#H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u000fR\u0011\u0010\n\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000fR\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006$"}, d2 = {"Lcom/officemitra/app/feature/home/HomeUiState;", "", "user", "Lcom/officemitra/app/data/model/User;", "isPunchedIn", "", "todayAttendance", "Lcom/officemitra/app/data/model/AttendanceRecord;", "unreadNotificationCount", "", "isRefreshing", "recentActivities", "", "Lcom/officemitra/app/data/model/AppNotification;", "(Lcom/officemitra/app/data/model/User;ZLcom/officemitra/app/data/model/AttendanceRecord;IZLjava/util/List;)V", "()Z", "getRecentActivities", "()Ljava/util/List;", "getTodayAttendance", "()Lcom/officemitra/app/data/model/AttendanceRecord;", "getUnreadNotificationCount", "()I", "getUser", "()Lcom/officemitra/app/data/model/User;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "", "app_release"})
public final class HomeUiState {
    @org.jetbrains.annotations.NotNull()
    private final com.officemitra.app.data.model.User user = null;
    private final boolean isPunchedIn = false;
    @org.jetbrains.annotations.NotNull()
    private final com.officemitra.app.data.model.AttendanceRecord todayAttendance = null;
    private final int unreadNotificationCount = 0;
    private final boolean isRefreshing = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.officemitra.app.data.model.AppNotification> recentActivities = null;

    public HomeUiState(@org.jetbrains.annotations.NotNull()
    com.officemitra.app.data.model.User user, boolean isPunchedIn, @org.jetbrains.annotations.NotNull()
    com.officemitra.app.data.model.AttendanceRecord todayAttendance, int unreadNotificationCount, boolean isRefreshing, @org.jetbrains.annotations.NotNull()
    java.util.List<com.officemitra.app.data.model.AppNotification> recentActivities) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final com.officemitra.app.data.model.User getUser() {
        return null;
    }

    public final boolean isPunchedIn() {
        return false;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.officemitra.app.data.model.AttendanceRecord getTodayAttendance() {
        return null;
    }

    public final int getUnreadNotificationCount() {
        return 0;
    }

    public final boolean isRefreshing() {
        return false;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.officemitra.app.data.model.AppNotification> getRecentActivities() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.officemitra.app.data.model.User component1() {
        return null;
    }

    public final boolean component2() {
        return false;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.officemitra.app.data.model.AttendanceRecord component3() {
        return null;
    }

    public final int component4() {
        return 0;
    }

    public final boolean component5() {
        return false;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.officemitra.app.data.model.AppNotification> component6() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.officemitra.app.feature.home.HomeUiState copy(@org.jetbrains.annotations.NotNull()
    com.officemitra.app.data.model.User user, boolean isPunchedIn, @org.jetbrains.annotations.NotNull()
    com.officemitra.app.data.model.AttendanceRecord todayAttendance, int unreadNotificationCount, boolean isRefreshing, @org.jetbrains.annotations.NotNull()
    java.util.List<com.officemitra.app.data.model.AppNotification> recentActivities) {
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