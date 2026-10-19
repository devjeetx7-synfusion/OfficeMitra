package com.officemitra.app.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class DemoRepository_Factory implements Factory<DemoRepository> {
  @Override
  public DemoRepository get() {
    return newInstance();
  }

  public static DemoRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DemoRepository newInstance() {
    return new DemoRepository();
  }

  private static final class InstanceHolder {
    private static final DemoRepository_Factory INSTANCE = new DemoRepository_Factory();
  }
}
