package com.officemitra.app.feature.approvals;

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
public final class ApprovalsViewModel_Factory implements Factory<ApprovalsViewModel> {
  private final Provider<DemoRepository> repositoryProvider;

  public ApprovalsViewModel_Factory(Provider<DemoRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ApprovalsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ApprovalsViewModel_Factory create(Provider<DemoRepository> repositoryProvider) {
    return new ApprovalsViewModel_Factory(repositoryProvider);
  }

  public static ApprovalsViewModel newInstance(DemoRepository repository) {
    return new ApprovalsViewModel(repository);
  }
}
