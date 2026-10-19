package com.officemitra.app.feature.notifications;

import com.officemitra.app.data.repository.DemoRepository;
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
public final class NotificationsViewModel_Factory implements Factory<NotificationsViewModel> {
  private final Provider<DemoRepository> repositoryProvider;

  public NotificationsViewModel_Factory(Provider<DemoRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public NotificationsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static NotificationsViewModel_Factory create(Provider<DemoRepository> repositoryProvider) {
    return new NotificationsViewModel_Factory(repositoryProvider);
  }

  public static NotificationsViewModel newInstance(DemoRepository repository) {
    return new NotificationsViewModel(repository);
  }
}
