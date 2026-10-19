package com.officemitra.app.feature.company;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u000fJ\u000e\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u000fJ\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u000fJ\u0014\u0010\u0016\u001a\u00020\r2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\r0\u0018R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0019"}, d2 = {"Lcom/officemitra/app/feature/company/JoinCompanyViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/officemitra/app/data/repository/DemoRepository;", "(Lcom/officemitra/app/data/repository/DemoRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/officemitra/app/feature/company/JoinCompanyUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "onCompanyCodeChanged", "", "code", "", "onEmployeeIdChanged", "id", "onFullNameChanged", "name", "onMobileChanged", "mobile", "submitJoinRequest", "onSuccess", "Lkotlin/Function0;", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class JoinCompanyViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.officemitra.app.data.repository.DemoRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.officemitra.app.feature.company.JoinCompanyUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.feature.company.JoinCompanyUiState> uiState = null;

    @javax.inject.Inject()
    public JoinCompanyViewModel(@org.jetbrains.annotations.NotNull()
    com.officemitra.app.data.repository.DemoRepository repository) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.feature.company.JoinCompanyUiState> getUiState() {
        return null;
    }

    public final void onCompanyCodeChanged(@org.jetbrains.annotations.NotNull()
    java.lang.String code) {
    }

    public final void onEmployeeIdChanged(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
    }

    public final void onFullNameChanged(@org.jetbrains.annotations.NotNull()
    java.lang.String name) {
    }

    public final void onMobileChanged(@org.jetbrains.annotations.NotNull()
    java.lang.String mobile) {
    }

    public final void submitJoinRequest(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess) {
    }
}