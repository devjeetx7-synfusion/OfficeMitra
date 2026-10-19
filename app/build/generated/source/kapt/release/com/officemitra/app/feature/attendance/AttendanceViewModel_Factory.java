package com.officemitra.app.feature.attendance;

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
public final class AttendanceViewModel_Factory implements Factory<AttendanceViewModel> {
  private final Provider<DemoRepository> repositoryProvider;

  public AttendanceViewModel_Factory(Provider<DemoRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AttendanceViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AttendanceViewModel_Factory create(Provider<DemoRepository> repositoryProvider) {
    return new AttendanceViewModel_Factory(repositoryProvider);
  }

  public static AttendanceViewModel newInstance(DemoRepository repository) {
    return new AttendanceViewModel(repository);
  }
}
