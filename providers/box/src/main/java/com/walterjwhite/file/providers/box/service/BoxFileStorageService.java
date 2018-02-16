package com.walterjwhite.file.providers.box.service;

import com.walterjwhite.datastore.criteria.Repository;
import com.walterjwhite.encryption.api.service.CompressionService;
import com.walterjwhite.encryption.api.service.DigestService;
import com.walterjwhite.encryption.api.service.EncryptionService;
import com.walterjwhite.file.api.model.File;
import com.walterjwhite.file.impl.service.AbstractFileStorageService;
import com.walterjwhite.google.guice.property.enumeration.Debug;
import com.walterjwhite.google.guice.property.enumeration.NoOperation;
import com.walterjwhite.google.guice.property.property.Property;
import java.io.IOException;
import javax.inject.Inject;

public class BoxFileStorageService extends AbstractFileStorageService {
  @Inject
  public BoxFileStorageService(
      CompressionService compressionService,
      EncryptionService encryptionService,
      DigestService digestService,
      Repository repository,
      @Property(NoOperation.class) boolean nop,
      @Property(Debug.class) boolean debug) {
    super(compressionService, encryptionService, digestService, repository, nop, debug);
  }

  @Override
  protected void doPut(File file) throws IOException {}

  @Override
  protected void doGet(File file) throws IOException {}

  @Override
  public void delete(File file) {}
}
