package com.walterjwhite.file.providers.local.service;

import com.walterjwhite.file.api.service.FileEntityOutputStream;
import com.walterjwhite.file.api.service.FileStorageService;
import com.walterjwhite.google.guice.GuiceHelper;
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
    if (!rereadSource.exists()) throw new IllegalStateException("file should exist.");

    // f.getSource().delete();
  }
}
