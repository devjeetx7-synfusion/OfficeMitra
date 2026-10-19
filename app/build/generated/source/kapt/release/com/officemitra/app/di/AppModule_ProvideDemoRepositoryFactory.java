package com.officemitra.app.di;

import com.officemitra.app.data.repository.DemoRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideDemoRepositoryFactory implements Factory<DemoRepository> {
  @Override
  public DemoRepository get() {
    return provideDemoRepository();
  }

  public static AppModule_ProvideDemoRepositoryFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DemoRepository provideDemoRepository() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDemoRepository());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideDemoRepositoryFactory INSTANCE = new AppModule_ProvideDemoRepositoryFactory();
  }
}
