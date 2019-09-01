package com.walterjwhite.file.providers.local.service;

import com.walterjwhite.property.api.annotation.DefaultValue;
import com.walterjwhite.property.api.property.ConfigurableProperty;

public interface LocalFileStoragePath extends ConfigurableProperty {
  @DefaultValue String Default = "/tmp/local-file-storage";
}
