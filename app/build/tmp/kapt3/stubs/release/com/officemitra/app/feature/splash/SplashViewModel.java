package com.officemitra.app.feature.splash;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\f\u001a\u00020\rH\u0002R\u0016\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/officemitra/app/feature/splash/SplashViewModel;", "Landroidx/lifecycle/ViewModel;", "userPreferences", "Lcom/officemitra/app/data/preferences/UserPreferences;", "(Lcom/officemitra/app/data/preferences/UserPreferences;)V", "_destination", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/officemitra/app/feature/splash/SplashDestination;", "destination", "Lkotlinx/coroutines/flow/StateFlow;", "getDestination", "()Lkotlinx/coroutines/flow/StateFlow;", "checkNavigationState", "", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SplashViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.officemitra.app.data.preferences.UserPreferences userPreferences = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.officemitra.app.feature.splash.SplashDestination> _destination = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.feature.splash.SplashDestination> destination = null;

    @javax.inject.Inject()
    public SplashViewModel(@org.jetbrains.annotations.NotNull()
    com.officemitra.app.data.preferences.UserPreferences userPreferences) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.officemitra.app.feature.splash.SplashDestination> getDestination() {
        return null;
    }

    private final void checkNavigationState() {
    }
}