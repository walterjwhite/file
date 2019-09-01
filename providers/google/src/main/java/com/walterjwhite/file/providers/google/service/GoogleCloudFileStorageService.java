package com.walterjwhite.file.providers.google.service;

import com.google.auth.Credentials;
import com.google.cloud.storage.*;
import com.walterjwhite.datastore.api.repository.Repository;
import com.walterjwhite.encryption.api.service.CompressionService;
import com.walterjwhite.encryption.service.DigestService;
import com.walterjwhite.encryption.service.EncryptionService;
import com.walterjwhite.file.api.model.File;
import com.walterjwhite.file.impl.service.AbstractFileStorageService;
import com.walterjwhite.file.providers.google.service.property.GoogleCloudBucket;
import com.walterjwhite.google.property.GoogleCloudProjectId;
import com.walterjwhite.property.api.enumeration.Debug;
import com.walterjwhite.property.api.enumeration.NoOperation;
import com.walterjwhite.property.impl.annotation.Property;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Provider;

public class GoogleCloudFileStorageService extends AbstractFileStorageService {
  protected final String bucketName;
  protected final Storage storage;
  protected final Bucket bucket;

  @Inject
  public GoogleCloudFileStorageService(
      CompressionService compressionService,
      EncryptionService encryptionService,
      DigestService digestService,
      Provider<Repository> repositoryProvider,
      @Property(NoOperation.class) boolean nop,
      @Property(Debug.class) boolean debug,
      @Property(GoogleCloudBucket.class) String bucketName,
      @Property(GoogleCloudProjectId.class) String projectId,
      Credentials credentials) {

    super(compressionService, encryptionService, digestService, repositoryProvider, nop, debug);

    this.bucketName = bucketName;

    storage =
        StorageOptions.newBuilder()
            .setCredentials(credentials)
            .setProjectId(projectId)
            .build()
            .getService();

    bucket = getOrCreateBucket(bucketName);
  }

  protected Bucket getOrCreateBucket(final String bucketName) {
    try {
      return (storage.create(BucketInfo.of(bucketName)));
    } catch (StorageException e) {
      handleBucketCreationException();
      return (storage.get(bucketName));
    }
  }

  protected void handleBucketCreationException() {}

  @Override
  protected void doPut(File file) throws FileNotFoundException {
    bucket.create(
        file.getChecksum(),
        new BufferedInputStream(new FileInputStream(new java.io.File(file.getSource()))));
  }

  @Override
  public void doGet(File file) throws IOException {
    Blob blob = bucket.get(file.getChecksum());
    final java.io.File outputFile = java.io.File.createTempFile(file.getChecksum(), "gcs");
    blob.downloadTo(outputFile.toPath());
    file.setSource(outputFile.getAbsolutePath());
  }

  @Override
  public void delete(File file) {
    bucket.get(file.getChecksum()).delete();
  }
}
