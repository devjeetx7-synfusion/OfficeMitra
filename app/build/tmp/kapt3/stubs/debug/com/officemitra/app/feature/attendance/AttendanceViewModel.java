package com.officemitra.app.feature.attendance;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\n\u001a\u00020\u000bJ\u001e\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\u0011"}, d2 = {"Lcom/officemitra/app/feature/attendance/AttendanceViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/officemitra/app/data/repository/DemoRepository;", "(Lcom/officemitra/app/data/repository/DemoRepository;)V", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/officemitra/app/feature/attendance/AttendanceUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "punchInOrOut", "", "submitCorrection", "date", "", "requestedTime", "reason", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AttendanceViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.officemitra.app.data.repository.DemoRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.feature.attendance.AttendanceUiState> uiState = null;

    @javax.inject.Inject()
    public AttendanceViewModel(@org.jetbrains.annotations.NotNull()
    com.officemitra.app.data.repository.DemoRepository repository) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.feature.attendance.AttendanceUiState> getUiState() {
        return null;
    }

    public final void punchInOrOut() {
    }

    public final void submitCorrection(@org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    java.lang.String requestedTime, @org.jetbrains.annotations.NotNull()
    java.lang.String reason) {
    }
}