package com.officemitra.app.feature.profile;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\u000fJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0012J\u001e\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/officemitra/app/feature/profile/ProfileViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/officemitra/app/data/repository/DemoRepository;", "userPreferences", "Lcom/officemitra/app/data/preferences/UserPreferences;", "(Lcom/officemitra/app/data/repository/DemoRepository;Lcom/officemitra/app/data/preferences/UserPreferences;)V", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/officemitra/app/feature/profile/ProfileUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "logout", "", "onLoggedOut", "Lkotlin/Function0;", "setThemeMode", "mode", "", "updateProfile", "name", "email", "phone", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ProfileViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.officemitra.app.data.repository.DemoRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.officemitra.app.data.preferences.UserPreferences userPreferences = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.feature.profile.ProfileUiState> uiState = null;

    @javax.inject.Inject()
    public ProfileViewModel(@org.jetbrains.annotations.NotNull()
    com.officemitra.app.data.repository.DemoRepository repository, @org.jetbrains.annotations.NotNull()
    com.officemitra.app.data.preferences.UserPreferences userPreferences) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.feature.profile.ProfileUiState> getUiState() {
        return null;
    }

    public final void updateProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String phone) {
    }

    public final void setThemeMode(@org.jetbrains.annotations.NotNull()
    java.lang.String mode) {
    }

    public final void logout(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onLoggedOut) {
    }
}