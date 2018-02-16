package com.walterjwhite.file.providers.google.service;

import com.google.inject.persist.jpa.JpaPersistModule;
import com.walterjwhite.compression.modules.CompressionModule;
import com.walterjwhite.datastore.GoogleGuicePersistModule;
import com.walterjwhite.datastore.criteria.CriteriaBuilderModule;
import com.walterjwhite.encryption.impl.EncryptionModule;
import com.walterjwhite.google.guice.GuiceHelper;
import com.walterjwhite.google.guice.property.test.GuiceTestModule;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoogleCloudFileStorageServiceTest {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(GoogleCloudFileStorageServiceTest.class);

  @Before
  public void onBefore() throws Exception {
    GuiceHelper.addModules(
        new GoogleCloudFileStorageModule(),
        new GuiceTestModule(),
        new JpaPersistModule("defaultJPAUnit"),
        new CriteriaBuilderModule(),
        new GoogleGuicePersistModule(),
        new EncryptionModule(),
        new CompressionModule());
    // GuiceHelper.setup();
  }

  @After
  public void onAfter() throws Exception {
    // GuiceHelper.stop();
  }

  @Test
  public void testBasics()
      throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
          IOException {
    /*
        GoogleCloudFileStorageService googleCloudFileStorageService =
            GuiceHelper.getGuiceInjector().getInstance(GoogleCloudFileStorageService.class);
        final File file = new File("/tmp/test");

        googleCloudFileStorageService.put(file);
        googleCloudFileStorageService.get(file);

        LOGGER.info("source:" + file.getSource());

        googleCloudFileStorageService.delete(file);
        LOGGER.info("deleted file");
    */
  }
}
