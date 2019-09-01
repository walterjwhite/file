package com.walterjwhite.file.providers.local.service;

import com.walterjwhite.file.api.service.FileStorageService;
import com.walterjwhite.google.guice.GuiceHelper;
import java.io.FileOutputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class LocalFileStorageServiceTest {

  protected FileStorageService fileStorageService;

  @Before
  public void before() {
    GuiceHelper.addModules(new LocalFileStorageTestModule(getClass()));
    GuiceHelper.setup();

    fileStorageService =
        GuiceHelper.getGuiceApplicationInjector().getInstance(FileStorageService.class);
  }

  @Test
  public void testSomething() throws Exception {
    final byte[] rawData = new byte[1024];
    for (int i = 0; i < rawData.length; i++) {
      rawData[i] = 0;
    }

    final java.io.File source = new java.io.File("/tmp/plain-test");
    IOUtils.write(rawData, new FileOutputStream(source));

    try {
      final com.walterjwhite.file.api.model.File f = fileStorageService.put(source);
      fileStorageService.get(f);

      final java.io.File rereadSource = new java.io.File(f.getSource());
      if (!rereadSource.exists()) throw (new IllegalStateException("file should exist."));
    } catch (Exception e) {
      LOGGER.warn("error", e);
    }

    source.delete();
  }
}
