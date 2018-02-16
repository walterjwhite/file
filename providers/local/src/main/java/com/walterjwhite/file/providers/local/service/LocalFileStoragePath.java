package com.walterjwhite.file.providers.local.service;

import com.walterjwhite.google.guice.property.property.DefaultValue;
import com.walterjwhite.google.guice.property.property.GuiceProperty;

public interface LocalFileStoragePath extends GuiceProperty {
  @DefaultValue String Default = "/tmp/local-file-storage";
}
