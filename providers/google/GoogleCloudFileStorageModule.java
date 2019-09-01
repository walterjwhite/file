package com.walterjwhite.file.providers.google.service;

import com.google.inject.AbstractModule;
import com.walterjwhite.file.api.service.FileStorageService;
import com.walterjwhite.google.GoogleModule;

public class GoogleCloudFileStorageModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(FileStorageService.class).to(GoogleCloudFileStorageService.class);

    install(new GoogleModule());
  }
}
