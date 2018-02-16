package com.walterjwhite.file.providers.amazon.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.walterjwhite.compression.modules.CompressionModule;
import com.walterjwhite.datastore.GoogleGuicePersistModule;
import com.walterjwhite.datastore.criteria.CriteriaBuilderModule;
import com.walterjwhite.encryption.impl.EncryptionModule;
import com.walterjwhite.file.api.service.FileStorageService;
import com.walterjwhite.google.guice.GuiceHelper;
import com.walterjwhite.google.guice.property.test.GuiceTestModule;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmazonS3FileStorageServiceTest {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(AmazonS3FileStorageServiceTest.class);

  protected FileStorageService fileStorageService;

  /**
   * AWS configuration must be done via properties, command-line, or finally from the environment
   */
  @Before
  public void onBefore() throws Exception {
    // TODO: configure JPA unit, do not hard-code
    GuiceHelper.addModules(
        new AmazonS3FileStorageModule(),
        new GuiceTestModule(),
        new CriteriaBuilderModule(),
        new GoogleGuicePersistModule(),
        new JpaPersistModule("defaultJPAUnit"),
        new CompressionModule(),
        new EncryptionModule());
    GuiceHelper.setup();

    fileStorageService = GuiceHelper.getGuiceInjector().getInstance(FileStorageService.class);
  }

  private static void set(final String key, final String value) {
    System.getProperties().setProperty(key, value);
  }

  @Test
  public void testFileCreation() throws Exception {
    final byte[] rawData = new byte[1024];
    for (int i = 0; i < rawData.length; i++) {
      rawData[i] = 0;
    }

    final File source = new File("/tmp/plain-test");
    IOUtils.write(rawData, new FileOutputStream(source));

    try {
      final com.walterjwhite.file.api.model.File f = fileStorageService.put(source);

      fileStorageService.get(f);

      final File rereadSource = new File(f.getSource());
      if (!rereadSource.exists()) throw (new IllegalStateException("file should exist."));
    } catch (AmazonS3Exception e) {
      LOGGER.error("error", e);
      for (final String key : e.getAdditionalDetails().keySet()) {
        LOGGER.info(key + ":" + e.getAdditionalDetails().get(key));
      }
    }
  }
}
