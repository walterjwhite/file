package com.walterjwhite.file.modules.cli;

import com.walterjwhite.compression.modules.CompressionModule;
import com.walterjwhite.datastore.GoogleGuicePersistModule;
import com.walterjwhite.datastore.api.repository.CriteriaBuilderModule;
import com.walterjwhite.encryption.impl.EncryptionModule;
import com.walterjwhite.file.modules.cli.enumeration.EncryptionOperatingMode;
import com.walterjwhite.file.providers.local.service.FileStorageModule;
import com.walterjwhite.google.guice.cli.AbstractCommandLineModule;
import com.walterjwhite.property.api.PropertyManager;
import org.reflections.Reflections;

public class FileCommandLineModule extends AbstractCommandLineModule {

  public FileCommandLineModule(PropertyManager propertyManager, Reflections reflections) {
    super(propertyManager, reflections, EncryptionOperatingMode.class);
  }

  /**
   * FYI, at this point, properties should be injected, so we can do a dynamic configuration here
   * ...
   */
  @Override
  protected void doCliConfigure() {
    install(new FileStorageModule());
    install(new EncryptionModule());
    install(new CompressionModule());

    install(new GoogleGuicePersistModule(/*propertyManager, reflections*/ ));
    install(new CriteriaBuilderModule());
  }
}
