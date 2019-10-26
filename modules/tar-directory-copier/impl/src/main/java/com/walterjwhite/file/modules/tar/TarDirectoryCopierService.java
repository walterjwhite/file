package com.walterjwhite.file.modules.tar;

import com.walterjwhite.file.api.service.DirectoryCopierService;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.commons.io.IOUtils;

public class TarDirectoryCopierService implements DirectoryCopierService {
  @Override
  public void copy(Path source, Path target) throws IOException {
    final Process tarCp =
        Runtime.getRuntime().exec(new String[] {"tar", "cp", "-C", source.toString(), "."});
    final Process tarXp =
        Runtime.getRuntime()
            .exec(new String[] {"tar", "xp", "--no-overwrite-dir", "-C", target.toString()});

    IOUtils.copy(tarCp.getInputStream(), tarXp.getOutputStream());
    final int returnCode;
    try {
      returnCode = tarCp.waitFor();
      if (returnCode > 0) {
        throw new IllegalStateException("Error copying files, non-zero return-code:" + returnCode);
      }
    } catch (InterruptedException e) {
      throw new RuntimeException("Error waiting for result");
    }

    // tarXp.waitFor();
    tarXp.getOutputStream().flush();
    tarXp.getOutputStream().close();

    tarCp.getInputStream().close();
  }
}
