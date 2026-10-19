package com.officemitra.app.feature.salary;

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
public final class SalaryViewModel_Factory implements Factory<SalaryViewModel> {
  private final Provider<DemoRepository> repositoryProvider;

  public SalaryViewModel_Factory(Provider<DemoRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SalaryViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static SalaryViewModel_Factory create(Provider<DemoRepository> repositoryProvider) {
    return new SalaryViewModel_Factory(repositoryProvider);
  }

  public static SalaryViewModel newInstance(DemoRepository repository) {
    return new SalaryViewModel(repository);
  }
}
