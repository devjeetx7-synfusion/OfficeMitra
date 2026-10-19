package com.officemitra.app.feature.auth;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0011J\u000e\u0010\u0013\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0011J\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0011J\u000e\u0010\u0015\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0011J\u0014\u0010\u0016\u001a\u00020\r2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\r0\u0018R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/officemitra/app/feature/auth/RegisterViewModel;", "Landroidx/lifecycle/ViewModel;", "userPreferences", "Lcom/officemitra/app/data/preferences/UserPreferences;", "(Lcom/officemitra/app/data/preferences/UserPreferences;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/officemitra/app/feature/auth/RegisterUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "onAcceptTermsChanged", "", "value", "", "onConfirmPasswordChanged", "", "onEmailChanged", "onFullNameChanged", "onMobileNumberChanged", "onPasswordChanged", "register", "onSuccess", "Lkotlin/Function0;", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class RegisterViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.officemitra.app.data.preferences.UserPreferences userPreferences = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.officemitra.app.feature.auth.RegisterUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.feature.auth.RegisterUiState> uiState = null;

    @javax.inject.Inject()
    public RegisterViewModel(@org.jetbrains.annotations.NotNull()
    com.officemitra.app.data.preferences.UserPreferences userPreferences) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.feature.auth.RegisterUiState> getUiState() {
        return null;
    }

    public final void onFullNameChanged(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }

    public final void onEmailChanged(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }

    public final void onMobileNumberChanged(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }

    public final void onPasswordChanged(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }

    public final void onConfirmPasswordChanged(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }

    public final void onAcceptTermsChanged(boolean value) {
    }

    public final void register(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess) {
    }
}