package com.officemitra.app.feature.auth;

import com.officemitra.app.data.preferences.UserPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class LoginViewModel_Factory implements Factory<LoginViewModel> {
  private final Provider<UserPreferences> userPreferencesProvider;

  public LoginViewModel_Factory(Provider<UserPreferences> userPreferencesProvider) {
    this.userPreferencesProvider = userPreferencesProvider;
  }

  @Override
  public LoginViewModel get() {
    return newInstance(userPreferencesProvider.get());
  }

  public static LoginViewModel_Factory create(Provider<UserPreferences> userPreferencesProvider) {
    return new LoginViewModel_Factory(userPreferencesProvider);
  }

  public static LoginViewModel newInstance(UserPreferences userPreferences) {
    return new LoginViewModel(userPreferences);
  }
}
