package com.walterjwhite.file.modules.tar;

import com.walterjwhite.file.api.service.DirectoryCopierService;
import java.io.IOException;
import java.nio.file.Path;

public class JavaNIODirectoryCopierService implements DirectoryCopierService {
  @Override
  public void copy(Path source, Path target) throws IOException {
    DirectoryCopier directoryCopier = new DirectoryCopier(source, target);
    java.nio.file.Files.walkFileTree(source, directoryCopier);
  }
}
