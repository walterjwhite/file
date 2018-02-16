package com.walterjwhite.file.providers.amazon.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.walterjwhite.amazon.property.AmazonRegion;
import com.walterjwhite.datastore.criteria.Repository;
import com.walterjwhite.encryption.api.service.CompressionService;
import com.walterjwhite.encryption.api.service.DigestService;
import com.walterjwhite.encryption.api.service.EncryptionService;
import com.walterjwhite.file.api.model.File;
import com.walterjwhite.file.impl.service.AbstractFileStorageService;
import com.walterjwhite.google.guice.property.enumeration.Debug;
import com.walterjwhite.google.guice.property.enumeration.NoOperation;
import com.walterjwhite.google.guice.property.enumeration.ProxyType;
import com.walterjwhite.google.guice.property.property.Property;
import com.walterjwhite.google.guice.property.property.ProxyHost;
import com.walterjwhite.google.guice.property.property.ProxyPort;
import java.io.FileOutputStream;
import javax.inject.Inject;

public class AmazonS3FileStorageService extends AbstractFileStorageService {
  // TODO: I think this should be a bit more flexible
  protected final String bucketName;

  protected final AmazonS3 s3client;

  @Inject
  public AmazonS3FileStorageService(
      CompressionService compressionService,
      EncryptionService encryptionService,
      DigestService digestService,
      Repository repository,
      @Property(NoOperation.class) boolean nop,
      @Property(Debug.class) boolean debug,
      @Property(com.walterjwhite.google.guice.property.property.ProxyType.class)
          ProxyType proxyType,
      @Property(ProxyHost.class) String proxyHost,
      @Property(ProxyPort.class) int proxyPort,
      @Property(AmazonS3Bucket.class) String bucketName,
      @Property(AmazonRegion.class) Regions region) {

    super(compressionService, encryptionService, digestService, repository, nop, debug);
    this.bucketName = bucketName;

    ClientConfiguration clientConfiguration = new ClientConfiguration();

    if (ProxyType.HTTP.equals(proxyType)) clientConfiguration.setProtocol(Protocol.HTTP);

    clientConfiguration.setProxyHost(proxyHost);
    clientConfiguration.setProxyPort(proxyPort);

    s3client =
        AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withClientConfiguration(clientConfiguration)
            .build();
  }

  //  protected void createBucket(final String bucketName) {
  //    try {
  //      s3client.createBucket(bucketName);
  //    } catch (Exception e) {
  //      LOGGER.error("error creating bucket", e);
  //    }
  //  }

  @Override
  protected void doPut(File file) {
    //    createBucket(bucketName);
    doAmazonPut(bucketName, file.getId(), file.getSource());
  }

  protected PutObjectResult doAmazonPut(
      final String bucketName, final String fileChecksum, final String fileSource) {
    return s3client.putObject(bucketName, fileChecksum, new java.io.File(fileSource));

    // I believe the below code only works if we enable versioning
    //    if (putObjectResult.getVersionId() == null || putObjectResult.getVersionId().length() ==
    // 0) {
    //      throw (new RuntimeException("Upload failed."));
    //    }
  }

  @Override
  public void doGet(File file) throws Exception {
    final S3Object s3Object = getS3Object(file);

    final java.io.File outputFile = java.io.File.createTempFile(file.getId(), "s3");

    try (final FileOutputStream fos = new FileOutputStream(outputFile)) {
      int read;
      while ((read = s3Object.getObjectContent().read()) >= 0) {
        fos.write(read);
      }

      file.setSource(outputFile.getAbsolutePath());
    }
  }

  protected S3Object getS3Object(File file) {
    return s3client.getObject(bucketName, file.getId());
  }

  @Override
  public void delete(File file) {
    s3client.deleteObject(bucketName, file.getId());
  }
}
