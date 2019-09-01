package com.walterjwhite.modules.file.local.guice;

import com.google.inject.AbstractModule;
import com.walterjwhite.file.api.service.FileStorageService;
import com.walterjwhite.file.providers.local.service.LocalFileStorageService;

public class LocalFileStorageModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(FileStorageService.class).to(LocalFileStorageService.class);
  }
}
