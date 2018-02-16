package com.walterjwhite.file.providers.amazon.service;

import com.google.inject.AbstractModule;
import com.walterjwhite.file.api.service.FileStorageService;

public class AmazonS3FileStorageModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(FileStorageService.class).to(AmazonS3FileStorageService.class);
  }
}
