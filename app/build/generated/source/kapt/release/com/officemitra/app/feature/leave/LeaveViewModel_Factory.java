package com.officemitra.app.feature.leave;

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
public final class LeaveViewModel_Factory implements Factory<LeaveViewModel> {
  private final Provider<DemoRepository> repositoryProvider;

  public LeaveViewModel_Factory(Provider<DemoRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public LeaveViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static LeaveViewModel_Factory create(Provider<DemoRepository> repositoryProvider) {
    return new LeaveViewModel_Factory(repositoryProvider);
  }

  public static LeaveViewModel newInstance(DemoRepository repository) {
    return new LeaveViewModel(repository);
  }
}
