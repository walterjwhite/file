package com.walterjwhite.file.providers.amazon.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.walterjwhite.file.api.service.FileStorageService;
import com.walterjwhite.google.guice.GuiceHelper;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

public class AmazonS3FileStorageServiceTest {
  protected FileStorageService fileStorageService;

  /**
   * AWS configuration must be done via properties, command-line, or finally from the environment
   */
  @Before
  public void onBefore() {
    GuiceHelper.addModules(
        new AmazonS3TestModule(
            getClass(),
            new Reflections(
                "com.walterjwhite",
                TypeAnnotationsScanner.class,
                SubTypesScanner.class,
                FieldAnnotationsScanner.class)));
    GuiceHelper.setup();

    fileStorageService =
        GuiceHelper.getGuiceApplicationInjector().getInstance(FileStorageService.class);
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
      if (!rereadSource.exists()) throw new IllegalStateException("file should exist.");
    } catch (AmazonS3Exception e) {
      LOGGER.error("error", e);
      for (final String key : e.getAdditionalDetails().keySet()) {
        LOGGER.info(key + ":" + e.getAdditionalDetails().get(key));
      }
    }
  }
}
