package com.walterjwhite.file.impl.service;

import com.google.inject.persist.Transactional;
import com.walterjwhite.datastore.criteria.Repository;
import com.walterjwhite.encryption.api.service.CompressionService;
import com.walterjwhite.encryption.api.service.DigestService;
import com.walterjwhite.encryption.api.service.EncryptionService;
import com.walterjwhite.file.api.model.File;
import com.walterjwhite.file.api.service.FileStorageService;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFileStorageService implements FileStorageService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileStorageService.class);

  protected final CompressionService compressionService;
  protected final EncryptionService encryptionService;
  protected final DigestService digestService;
  protected final Repository repository;

  protected final boolean nop;
  protected final boolean debug;

  protected AbstractFileStorageService(
      CompressionService compressionService,
      EncryptionService encryptionService,
      DigestService digestService,
      Repository repository,
      boolean nop,
      boolean debug) {
    super();
    this.compressionService = compressionService;
    this.encryptionService = encryptionService;
    this.digestService = digestService;
    this.repository = repository;
    this.nop = nop;
    this.debug = debug;
  }

  @Transactional
  @Override
  public void put(File file)
      throws IOException, InvalidAlgorithmParameterException, InvalidKeyException,
          NoSuchAlgorithmException {
    final java.io.File sourceFile = new java.io.File(file.getSource());
    final String digest = digestService.compute(sourceFile);
    file.setChecksum(digest);
    repository.persist(file);

    final java.io.File replacedFile;
    if (!debug) {
      replacedFile = compressAndEncrypt(file, sourceFile);
    } else {
      replacedFile = null;
    }

    doPut(file);

    if (replacedFile != null) replacedFile.delete();
  }

  protected java.io.File compressAndEncrypt(File file, final java.io.File sourceFile)
      throws IOException, InvalidAlgorithmParameterException, InvalidKeyException {
    // do not read file into memory, this is inefficient
    final byte[] compressedData =
        compressionService.compress(FileUtils.readFileToByteArray(sourceFile));
    final byte[] encryptedData = encryptionService.encrypt(compressedData);

    final java.io.File compressedAndEncryptedFile = java.io.File.createTempFile("encrypted", "xz");
    FileUtils.writeByteArrayToFile(compressedAndEncryptedFile, encryptedData);
    file.setSource(compressedAndEncryptedFile.getAbsolutePath());

    sourceFile.delete();

    return (compressedAndEncryptedFile);
  }

  protected abstract void doPut(File file) throws IOException;

  @Override
  public File put(java.io.File file) throws Exception {
    File wFile = new File(file.getAbsolutePath());
    put(wFile);
    return (wFile);
  }

  @Override
  public void get(File file) throws Exception {
    doGet(file);

    final java.io.File sourceFile = new java.io.File(file.getSource());

    if (!debug) {
      final byte[] decryptedData =
          encryptionService.decrypt(FileUtils.readFileToByteArray(sourceFile));
      final byte[] decompressedData = compressionService.decompress(decryptedData);

      final java.io.File decryptedAndDecompressedFile =
          java.io.File.createTempFile("decrypted", "xz");
      FileUtils.writeByteArrayToFile(decryptedAndDecompressedFile, decompressedData);
      file.setSource(decryptedAndDecompressedFile.getAbsolutePath());
      sourceFile.delete();
    }
  }

  protected abstract void doGet(File file) throws Exception;
}
