package com.officemitra.app.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0018\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J(\u00105\u001a\u0002062\u0006\u00107\u001a\u0002022\u0006\u00108\u001a\u0002022\u0006\u00109\u001a\u0002022\u0006\u0010:\u001a\u000202H\u0002J.\u0010;\u001a\u0002062\u0006\u0010<\u001a\u0002022\u0006\u0010=\u001a\u0002022\u0006\u0010>\u001a\u0002022\u0006\u0010?\u001a\u00020@2\u0006\u0010A\u001a\u000202J\u000e\u0010B\u001a\u0002062\u0006\u0010C\u001a\u000202J\u0006\u0010D\u001a\u000206J\b\u0010E\u001a\u000202H\u0002J\b\u0010F\u001a\u000202H\u0002J\u0006\u0010G\u001a\u000206J\u000e\u0010H\u001a\u0002062\u0006\u0010I\u001a\u000202J\u001e\u0010J\u001a\u0002062\u0006\u0010K\u001a\u0002022\u0006\u0010L\u001a\u0002022\u0006\u0010A\u001a\u000202J\u000e\u0010M\u001a\u0002062\u0006\u0010N\u001a\u00020\u000eJ\u0012\u0010O\u001a\u0002062\n\b\u0002\u0010P\u001a\u0004\u0018\u000102J\"\u0010Q\u001a\u0002062\u0006\u0010C\u001a\u0002022\u0006\u0010R\u001a\u00020\u000e2\n\b\u0002\u0010S\u001a\u0004\u0018\u000102J\u001e\u0010T\u001a\u0002062\u0006\u0010U\u001a\u0002022\u0006\u0010V\u001a\u0002022\u0006\u0010W\u001a\u000202R\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\b0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u001d\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00050\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001bR\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\n0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001bR\u0011\u0010 \u001a\u00020!\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u001d\u0010$\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00050\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001bR\u0017\u0010&\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001bR\u0017\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00100\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001bR\u001d\u0010)\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00050\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001bR\u001d\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00050\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001bR\u001d\u0010-\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00050\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\u001bR\u0017\u0010/\u001a\b\u0012\u0004\u0012\u00020\b0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010\u001bR\u0014\u00101\u001a\u000202X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u00104\u00a8\u0006X"}, d2 = {"Lcom/officemitra/app/data/repository/DemoRepository;", "", "()V", "_approvalRequests", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/officemitra/app/data/model/ApprovalRequest;", "_attendanceHistory", "Lcom/officemitra/app/data/model/AttendanceRecord;", "_currentUser", "Lcom/officemitra/app/data/model/User;", "_holidays", "Lcom/officemitra/app/data/model/Holiday;", "_isPunchedIn", "", "_leaveBalance", "Lcom/officemitra/app/data/model/LeaveBalance;", "_leaveRequests", "Lcom/officemitra/app/data/model/LeaveRequest;", "_notifications", "Lcom/officemitra/app/data/model/AppNotification;", "_salarySlips", "Lcom/officemitra/app/data/model/SalarySlip;", "_todayAttendance", "approvalRequests", "Lkotlinx/coroutines/flow/StateFlow;", "getApprovalRequests", "()Lkotlinx/coroutines/flow/StateFlow;", "attendanceHistory", "getAttendanceHistory", "currentUser", "getCurrentUser", "demoCompanyInfo", "Lcom/officemitra/app/data/model/CompanyInfo;", "getDemoCompanyInfo", "()Lcom/officemitra/app/data/model/CompanyInfo;", "holidays", "getHolidays", "isPunchedIn", "leaveBalance", "getLeaveBalance", "leaveRequests", "getLeaveRequests", "notifications", "getNotifications", "salarySlips", "getSalarySlips", "todayAttendance", "getTodayAttendance", "validCompanyCode", "", "getValidCompanyCode", "()Ljava/lang/String;", "addNotification", "", "title", "message", "category", "route", "applyLeave", "leaveType", "startDate", "endDate", "daysCount", "", "reason", "cancelLeaveRequest", "requestId", "clearNotifications", "getCurrentDateFormatted", "getCurrentTimeFormatted", "markAllNotificationsAsRead", "markNotificationAsRead", "id", "submitAttendanceCorrection", "date", "requestedCorrection", "toggleAuthorizedApproverRole", "isApprover", "togglePunchInStatus", "photoUrl", "updateApprovalStatus", "isApproved", "comment", "updateProfile", "name", "email", "phone", "app_debug"})
public final class DemoRepository {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.officemitra.app.data.model.User> _currentUser = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.data.model.User> currentUser = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isPunchedIn = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isPunchedIn = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.officemitra.app.data.model.AttendanceRecord> _todayAttendance = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.data.model.AttendanceRecord> todayAttendance = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.officemitra.app.data.model.AttendanceRecord>> _attendanceHistory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.officemitra.app.data.model.AttendanceRecord>> attendanceHistory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.officemitra.app.data.model.LeaveBalance> _leaveBalance = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.data.model.LeaveBalance> leaveBalance = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.officemitra.app.data.model.LeaveRequest>> _leaveRequests = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.officemitra.app.data.model.LeaveRequest>> leaveRequests = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.officemitra.app.data.model.ApprovalRequest>> _approvalRequests = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.officemitra.app.data.model.ApprovalRequest>> approvalRequests = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.officemitra.app.data.model.Holiday>> _holidays = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.officemitra.app.data.model.Holiday>> holidays = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.officemitra.app.data.model.SalarySlip>> _salarySlips = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.officemitra.app.data.model.SalarySlip>> salarySlips = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.officemitra.app.data.model.AppNotification>> _notifications = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.officemitra.app.data.model.AppNotification>> notifications = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String validCompanyCode = "OM-10245";
    @org.jetbrains.annotations.NotNull()
    private final com.officemitra.app.data.model.CompanyInfo demoCompanyInfo = null;

    @javax.inject.Inject()
    public DemoRepository() {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.data.model.User> getCurrentUser() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isPunchedIn() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.data.model.AttendanceRecord> getTodayAttendance() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.officemitra.app.data.model.AttendanceRecord>> getAttendanceHistory() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.data.model.LeaveBalance> getLeaveBalance() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.officemitra.app.data.model.LeaveRequest>> getLeaveRequests() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.officemitra.app.data.model.ApprovalRequest>> getApprovalRequests() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.officemitra.app.data.model.Holiday>> getHolidays() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.officemitra.app.data.model.SalarySlip>> getSalarySlips() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.officemitra.app.data.model.AppNotification>> getNotifications() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getValidCompanyCode() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.officemitra.app.data.model.CompanyInfo getDemoCompanyInfo() {
        return null;
    }

    public final void togglePunchInStatus(@org.jetbrains.annotations.Nullable()
    java.lang.String photoUrl) {
    }

    public final void applyLeave(@org.jetbrains.annotations.NotNull()
    java.lang.String leaveType, @org.jetbrains.annotations.NotNull()
    java.lang.String startDate, @org.jetbrains.annotations.NotNull()
    java.lang.String endDate, int daysCount, @org.jetbrains.annotations.NotNull()
    java.lang.String reason) {
    }

    public final void cancelLeaveRequest(@org.jetbrains.annotations.NotNull()
    java.lang.String requestId) {
    }

    public final void submitAttendanceCorrection(@org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    java.lang.String requestedCorrection, @org.jetbrains.annotations.NotNull()
    java.lang.String reason) {
    }

    public final void updateApprovalStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String requestId, boolean isApproved, @org.jetbrains.annotations.Nullable()
    java.lang.String comment) {
    }

    public final void markNotificationAsRead(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
    }

    public final void markAllNotificationsAsRead() {
    }

    public final void clearNotifications() {
    }

    public final void updateProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String phone) {
    }

    public final void toggleAuthorizedApproverRole(boolean isApprover) {
    }

    private final void addNotification(java.lang.String title, java.lang.String message, java.lang.String category, java.lang.String route) {
    }

    private final java.lang.String getCurrentDateFormatted() {
        return null;
    }

    private final java.lang.String getCurrentTimeFormatted() {
        return null;
    }
}