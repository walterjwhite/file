package com.walterjwhite.file.providers.local.service;

import com.google.inject.AbstractModule;
import com.walterjwhite.file.api.service.FileStorageService;

public class FileStorageModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(FileStorageService.class).to(LocalFileStorageService.class);
  }
}
