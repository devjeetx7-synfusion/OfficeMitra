package com.officemitra.app.feature.holidays;

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
public final class HolidaysViewModel_Factory implements Factory<HolidaysViewModel> {
  private final Provider<DemoRepository> repositoryProvider;

  public HolidaysViewModel_Factory(Provider<DemoRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public HolidaysViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static HolidaysViewModel_Factory create(Provider<DemoRepository> repositoryProvider) {
    return new HolidaysViewModel_Factory(repositoryProvider);
  }

  public static HolidaysViewModel newInstance(DemoRepository repository) {
    return new HolidaysViewModel(repository);
  }
}
