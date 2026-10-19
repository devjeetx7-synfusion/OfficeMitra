package com.officemitra.app.feature.approvals;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\"\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\r2\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\u0013"}, d2 = {"Lcom/officemitra/app/feature/approvals/ApprovalsViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/officemitra/app/data/repository/DemoRepository;", "(Lcom/officemitra/app/data/repository/DemoRepository;)V", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/officemitra/app/feature/approvals/ApprovalsUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "toggleApproverRole", "", "isApprover", "", "updateApprovalStatus", "requestId", "", "isApproved", "comment", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ApprovalsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.officemitra.app.data.repository.DemoRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.feature.approvals.ApprovalsUiState> uiState = null;

    @javax.inject.Inject()
    public ApprovalsViewModel(@org.jetbrains.annotations.NotNull()
    com.officemitra.app.data.repository.DemoRepository repository) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.feature.approvals.ApprovalsUiState> getUiState() {
        return null;
    }

    public final void toggleApproverRole(boolean isApprover) {
    }

    public final void updateApprovalStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String requestId, boolean isApproved, @org.jetbrains.annotations.Nullable()
    java.lang.String comment) {
    }
}