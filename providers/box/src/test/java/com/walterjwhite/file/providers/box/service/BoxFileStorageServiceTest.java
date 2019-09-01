package com.walterjwhite.file.providers.box.service;

import com.walterjwhite.file.api.service.FileStorageService;
import com.walterjwhite.google.guice.GuiceHelper;
import org.junit.Before;
import org.junit.Test;

public class BoxFileStorageServiceTest {
  protected FileStorageService fileStorageService;

  @Before
  public void onBefore() {
    GuiceHelper.addModules(new BoxTestModule(getClass()));
    GuiceHelper.setup();

    fileStorageService =
        GuiceHelper.getGuiceApplicationInjector().getInstance(FileStorageService.class);
  }

  protected void set(final String key, final String value) {
    System.getProperties().setProperty(key, value);
  }

  @Test
  public void testFileCreation() {}
}
