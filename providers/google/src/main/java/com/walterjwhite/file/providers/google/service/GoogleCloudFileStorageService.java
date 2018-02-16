package com.walterjwhite.file.providers.google.service;

import com.google.auth.Credentials;
import com.google.cloud.storage.*;
import com.walterjwhite.datastore.criteria.Repository;
import com.walterjwhite.encryption.api.service.CompressionService;
import com.walterjwhite.encryption.api.service.DigestService;
import com.walterjwhite.encryption.api.service.EncryptionService;
import com.walterjwhite.file.api.model.File;
import com.walterjwhite.file.impl.service.AbstractFileStorageService;
import com.walterjwhite.file.providers.google.service.property.GoogleCloudBucket;
import com.walterjwhite.google.guice.property.enumeration.Debug;
import com.walterjwhite.google.guice.property.enumeration.NoOperation;
import com.walterjwhite.google.guice.property.property.Property;
import com.walterjwhite.google.property.GoogleCloudProjectId;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoogleCloudFileStorageService extends AbstractFileStorageService {
  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCloudFileStorageService.class);

  protected final String bucketName;
  protected final Storage storage;
  protected final Bucket bucket;

  @Inject
  public GoogleCloudFileStorageService(
      CompressionService compressionService,
      EncryptionService encryptionService,
      DigestService digestService,
      Repository repository,
      @Property(NoOperation.class) boolean nop,
      @Property(Debug.class) boolean debug,
      @Property(GoogleCloudBucket.class) String bucketName,
      @Property(GoogleCloudProjectId.class) String projectId,
      Credentials credentials) {

    super(compressionService, encryptionService, digestService, repository, nop, debug);

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
      LOGGER.debug("error creating bucket", e);
      return (storage.get(bucketName));
    }
  }

  @Override
  protected void doPut(File file) throws FileNotFoundException {
    LOGGER.info("file bucket:" + bucketName);
    LOGGER.info("file checksum:" + file.getId());
    LOGGER.info("file source:" + file.getSource());

    bucket.create(file.getId(), new FileInputStream(new java.io.File(file.getSource())));
  }

  @Override
  public void doGet(File file) throws IOException {
    Blob blob = bucket.get(file.getId());
    final java.io.File outputFile = java.io.File.createTempFile(file.getId(), "gcs");
    blob.downloadTo(outputFile.toPath());
    file.setSource(outputFile.getAbsolutePath());

    LOGGER.info("downloaded to:" + outputFile.getAbsolutePath());
  }

  @Override
  public void delete(File file) {
    bucket.get(file.getId()).delete();
  }
}
