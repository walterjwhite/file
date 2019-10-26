package com.walterjwhite.file.providers.amazon.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.walterjwhite.amazon.property.AmazonRegion;
import com.walterjwhite.datastore.api.repository.Repository;
import com.walterjwhite.encryption.api.service.CompressionService;
import com.walterjwhite.encryption.service.DigestService;
import com.walterjwhite.encryption.service.EncryptionService;
import com.walterjwhite.file.api.model.File;
import com.walterjwhite.file.impl.service.AbstractFileStorageService;
import com.walterjwhite.file.providers.amazon.property.AmazonS3Bucket;
import com.walterjwhite.property.api.enumeration.Debug;
import com.walterjwhite.property.api.enumeration.NoOperation;
import com.walterjwhite.property.api.property.ProxyHost;
import com.walterjwhite.property.api.property.ProxyPort;
import com.walterjwhite.property.api.property.ProxyType;
import com.walterjwhite.property.impl.annotation.Property;
import java.io.FileOutputStream;
import javax.inject.Inject;
import javax.inject.Provider;

public class AmazonS3FileStorageService extends AbstractFileStorageService {
  // TODO: I think this should be a bit more flexible
  protected final String bucketName;

  protected final AmazonS3 s3client;

  @Inject
  public AmazonS3FileStorageService(
      CompressionService compressionService,
      EncryptionService encryptionService,
      DigestService digestService,
      Provider<Repository> repositoryProvider,
      @Property(NoOperation.class) boolean nop,
      @Property(Debug.class) boolean debug,
      @Property(ProxyType.class) com.walterjwhite.property.api.enumeration.ProxyType proxyType,
      @Property(ProxyHost.class) String proxyHost,
      @Property(ProxyPort.class) int proxyPort,
      @Property(AmazonS3Bucket.class) String bucketName,
      @Property(AmazonRegion.class) Regions region) {

    super(compressionService, encryptionService, digestService, repositoryProvider, nop, debug);
    this.bucketName = bucketName;

    ClientConfiguration clientConfiguration = new ClientConfiguration();

    setupProxy(clientConfiguration, proxyType, proxyHost, proxyPort);

    s3client =
        AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withClientConfiguration(clientConfiguration)
            .build();
  }

  protected void setupProxy(
      ClientConfiguration clientConfiguration,
      com.walterjwhite.property.api.enumeration.ProxyType proxyType,
      String proxyHost,
      int proxyPort) {
    if (com.walterjwhite.property.api.enumeration.ProxyType.HTTP.equals(proxyType))
      clientConfiguration.setProtocol(Protocol.HTTP);

    clientConfiguration.setProxyHost(proxyHost);
    clientConfiguration.setProxyPort(proxyPort);
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
    doAmazonPut(bucketName, file.getChecksum() /*.getId()*/, file.getSource());
  }

  protected PutObjectResult doAmazonPut(
      final String bucketName, final String fileChecksum, final String fileSource) {
    return s3client.putObject(bucketName, fileChecksum, new java.io.File(fileSource));

    // I believe the below code only works if we enable versioning
    //    if (putObjectResult.getVersionId() == null || putObjectResult.getVersionId().length() ==
    // 0) {
    //      throw new RuntimeException("Upload failed.");
    //    }
  }

  @Override
  public void doGet(File file) throws Exception {
    final S3Object s3Object = getS3Object(file);

    final java.io.File outputFile =
        java.io.File.createTempFile(file.getChecksum() /*.getId()*/, "s3");

    try (final FileOutputStream fos = new FileOutputStream(outputFile)) {
      int read;
      while ((read = s3Object.getObjectContent().read()) >= 0) {
        fos.write(read);
      }

      file.setSource(outputFile.getAbsolutePath());
    }
  }

  protected S3Object getS3Object(File file) {
    return s3client.getObject(bucketName, file.getChecksum() /*.getId()*/);
  }

  @Override
  public void delete(File file) {
    s3client.deleteObject(bucketName, file.getChecksum() /*.getId()*/);
  }
}
