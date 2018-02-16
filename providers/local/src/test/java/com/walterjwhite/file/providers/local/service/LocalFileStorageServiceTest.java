package com.walterjwhite.file.providers.local.service;

import com.google.inject.persist.jpa.JpaPersistModule;
import com.walterjwhite.compression.modules.CompressionModule;
import com.walterjwhite.datastore.GoogleGuicePersistModule;
import com.walterjwhite.datastore.criteria.CriteriaBuilderModule;
import com.walterjwhite.encryption.impl.EncryptionModule;
import com.walterjwhite.file.api.service.FileEntityOutputStream;
import com.walterjwhite.file.api.service.FileStorageService;
import com.walterjwhite.google.guice.GuiceHelper;
import com.walterjwhite.google.guice.property.test.GuiceTestModule;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalFileStorageServiceTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileStorageServiceTest.class);

  protected FileStorageService fileStorageService;

  @Before
  public void before() throws Exception {
    GuiceHelper.addModules(
        new FileStorageModule(),
        new GuiceTestModule(),
        new CriteriaBuilderModule(),
        new GoogleGuicePersistModule(),
        new JpaPersistModule("defaultJPAUnit"),
        new CompressionModule(),
        new EncryptionModule());

    GuiceHelper.setup();

    fileStorageService = GuiceHelper.getGuiceInjector().getInstance(FileStorageService.class);
  }

  @Test
  public void testSomething() throws Exception {
    final byte[] rawData = new byte[1024];
    for (int i = 0; i < rawData.length; i++) {
      rawData[i] = 0;
    }

    final FileEntityOutputStream fileEntityOutputStream =
        new FileEntityOutputStream(fileStorageService);
    //    IOUtils.write(rawData, fileEntityOutputStream);
    fileEntityOutputStream.write(rawData);
    fileEntityOutputStream.flush();
    fileEntityOutputStream.close();

    final com.walterjwhite.file.api.model.File f = fileEntityOutputStream.getFile();

    LOGGER.info("f:" + f.getId());
    LOGGER.info("f:" + f.getSource());

    fileStorageService.get(f);

    final java.io.File rereadSource = new java.io.File(f.getSource());
    if (!rereadSource.exists()) throw (new IllegalStateException("file should exist."));

    // f.getSource().delete();
  }
}
