package com.walterjwhite.file.providers.box.service;

import com.google.inject.AbstractModule;
import com.walterjwhite.file.api.service.FileStorageService;

public class BoxFileStorageModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(FileStorageService.class).to(BoxFileStorageService.class);
  }
}
