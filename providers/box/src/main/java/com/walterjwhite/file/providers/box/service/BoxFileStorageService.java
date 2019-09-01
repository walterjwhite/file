package com.walterjwhite.file.providers.box.service;

import com.walterjwhite.datastore.api.repository.Repository;
import com.walterjwhite.encryption.api.service.CompressionService;
import com.walterjwhite.encryption.service.DigestService;
import com.walterjwhite.encryption.service.EncryptionService;
import com.walterjwhite.file.api.model.File;
import com.walterjwhite.file.impl.service.AbstractFileStorageService;
import com.walterjwhite.property.api.enumeration.Debug;
import com.walterjwhite.property.api.enumeration.NoOperation;
import com.walterjwhite.property.impl.annotation.Property;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Provider;

public class BoxFileStorageService extends AbstractFileStorageService {
  @Inject
  public BoxFileStorageService(
      CompressionService compressionService,
      EncryptionService encryptionService,
      DigestService digestService,
      Provider<Repository> repositoryProvider,
      @Property(NoOperation.class) boolean nop,
      @Property(Debug.class) boolean debug) {
    super(compressionService, encryptionService, digestService, repositoryProvider, nop, debug);
  }

  @Override
  protected void doPut(File file) throws IOException {}

  @Override
  protected void doGet(File file) throws IOException {}

  @Override
  public void delete(File file) {}
}
