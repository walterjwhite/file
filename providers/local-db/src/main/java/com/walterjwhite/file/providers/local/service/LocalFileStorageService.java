package com.walterjwhite.file.providers.local.service;

import com.google.inject.persist.Transactional;
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
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: this should be tied directly to a database to ensure we have the "file"
public class LocalFileStorageService extends AbstractFileStorageService {
  private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileStorageService.class);

  protected final FileDBEntryRepository fileDBEntryRepository;

  @Inject
  public LocalFileStorageService(
      CompressionService compressionService,
      EncryptionService encryptionService,
      DigestService digestService,
      FileDBEntryRepository fileDBEntryRepository,
      Repository repository,
      @Property(NoOperation.class) boolean nop,
      @Property(Debug.class) boolean debug) {
    super(compressionService, encryptionService, digestService, repository, nop, debug);
    this.fileDBEntryRepository = fileDBEntryRepository;
  }

  @Transactional
  @Override
  protected void doPut(File file) throws IOException {
    LOGGER.info("source:" + file.getSource());
    //    FileUtils.copyFile(new java.io.File(file.getSource()), getFile(file));

    // TODO: this will blow up the JVM with large files
    // instead we need to stream it to the db
    fileDBEntryRepository.persist(
        new FileDBEntry(
            file.getId(), FileUtils.readFileToByteArray(new java.io.File(file.getSource()))));
  }

  @Override
  protected void doGet(File file) throws IOException {
    final java.io.File outputFile = java.io.File.createTempFile(file.getId(), "local");

    FileDBEntry fileDBEntry = fileDBEntryRepository.findByFile(file);
    FileUtils.writeByteArrayToFile(outputFile, fileDBEntry.getData());

    file.setSource(outputFile.getAbsolutePath());
  }

  @Transactional
  @Override
  public void delete(File file) {
    try {
      //      getFile(file).delete();
      FileDBEntry fileDBEntry = fileDBEntryRepository.findByFile(file);
      fileDBEntryRepository.delete(fileDBEntry);
    } catch (Exception e) {
      LOGGER.error("Error deleting message", e);
      throw (new RuntimeException("Error deleting message", e));
    }
  }
}
