package com.walterjwhite.file.api.service;

import com.walterjwhite.file.api.model.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class FileUtil {
  private FileUtil() {}

  public static File store(
      final FileStorageService fileStorageService,
      final InputStream sourceInputStream,
      final String filename)
      throws IOException {
    try (final FileEntityOutputStream feos = new FileEntityOutputStream(fileStorageService)) {
      IOUtils.copy(sourceInputStream, feos);
      feos.flush();

      com.walterjwhite.file.api.model.File file = feos.getFile();
      file.withFilename(filename);

      return file;
    }
  }
}
