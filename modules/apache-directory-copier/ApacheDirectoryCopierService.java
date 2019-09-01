package com.walterjwhite.file.modules.apache;

import com.walterjwhite.file.api.service.DirectoryCopierService;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;

public class ApacheDirectoryCopierService implements DirectoryCopierService {
  @Override
  public void copy(Path source, Path target) throws IOException {
    FileUtils.copyDirectory(source.toFile(), target.toFile());
  }
}
