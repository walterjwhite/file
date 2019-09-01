package com.walterjwhite.file.api.service;

import java.io.IOException;
import java.nio.file.Path;

public interface DirectoryCopierService {
  void copy(final Path source, final Path target) throws IOException;
}
