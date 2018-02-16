package com.walterjwhite.file.providers.box.service;

import com.google.inject.persist.jpa.JpaPersistModule;
import com.walterjwhite.compression.modules.CompressionModule;
import com.walterjwhite.datastore.GoogleGuicePersistModule;
import com.walterjwhite.datastore.criteria.CriteriaBuilderModule;
import com.walterjwhite.encryption.impl.EncryptionModule;
import com.walterjwhite.file.api.service.FileStorageService;
import com.walterjwhite.google.guice.GuiceHelper;
import com.walterjwhite.google.guice.property.test.GuiceTestModule;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoxFileStorageServiceTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(BoxFileStorageServiceTest.class);

  protected FileStorageService fileStorageService;

  @Before
  public void onBefore() throws Exception {
    //    set("", "");

    GuiceHelper.addModules(
        new BoxFileStorageModule(),
        new GuiceTestModule(),
        new CriteriaBuilderModule(),
        new GoogleGuicePersistModule(),
        new JpaPersistModule("defaultJPAUnit"),
        new CompressionModule(),
        new EncryptionModule());
    GuiceHelper.setup();

    fileStorageService = GuiceHelper.getGuiceInjector().getInstance(FileStorageService.class);
  }

  protected void set(final String key, final String value) {
    System.getProperties().setProperty(key, value);
  }

  @Test
  public void testFileCreation() throws Exception {}
}
