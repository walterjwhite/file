package com.walterjwhite.file.providers.google.service;

import com.walterjwhite.compression.modules.CompressionModule;
import com.walterjwhite.datastore.GoogleGuicePersistModule;
import com.walterjwhite.datastore.api.repository.CriteriaBuilderModule;
import com.walterjwhite.encryption.impl.EncryptionModule;
import com.walterjwhite.file.impl.service.DefaultFileStorageModule;
import com.walterjwhite.google.guice.property.test.GuiceTestModule;
import com.walterjwhite.google.guice.property.test.PropertyValuePair;
import org.reflections.Reflections;

public class GoogleCloudFileStorageTestModule extends GuiceTestModule {
  public GoogleCloudFileStorageTestModule(
      Class testClass, PropertyValuePair... propertyValuePairs) {
    super(testClass, propertyValuePairs);
  }

  public GoogleCloudFileStorageTestModule(
      Class testClass, Reflections reflections, PropertyValuePair... propertyValuePairs) {
    super(testClass, reflections, propertyValuePairs);
  }

  @Override
  protected void configure() {
    super.configure();

    install(new GoogleCloudFileStorageModule());
    install(new DefaultFileStorageModule());
    install(new EncryptionModule());
    install(new CompressionModule());
    install(new CriteriaBuilderModule());
    install(new GoogleGuicePersistModule(/*propertyManager, reflections*/ ));
  }
}
