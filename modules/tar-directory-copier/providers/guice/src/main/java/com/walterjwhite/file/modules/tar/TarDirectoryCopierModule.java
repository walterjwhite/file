package com.walterjwhite.file.modules.tar;

import com.google.inject.AbstractModule;
import com.walterjwhite.file.api.service.DirectoryCopierService;

public class TarDirectoryCopierModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(DirectoryCopierService.class).to(TarDirectoryCopierService.class);
  }
}
