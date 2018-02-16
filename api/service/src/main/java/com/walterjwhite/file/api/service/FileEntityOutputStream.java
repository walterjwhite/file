package com.walterjwhite.file.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileEntityOutputStream extends OutputStream {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileEntityOutputStream.class);

  protected final File tempFile;
  protected final FileOutputStream outputStream;

  protected final FileStorageService fileStorageService;

  protected com.walterjwhite.file.api.model.File file;

  public FileEntityOutputStream(FileStorageService fileStorageService) throws IOException {
    super();

    this.fileStorageService = fileStorageService;
    tempFile = File.createTempFile("temp", "tmp");
    outputStream = new FileOutputStream(tempFile);
  }

  @Override
  public void write(byte[] bytes) throws IOException {
    outputStream.write(bytes);
  }

  @Override
  public void write(byte[] bytes, int i, int i1) throws IOException {
    outputStream.write(bytes, i, i1);
  }

  @Override
  public void close() throws IOException {
    outputStream.close();
  }

  @Override
  public void write(int i) throws IOException {
    outputStream.write(i);
  }

  @Override
  public void flush() throws IOException {
    LOGGER.info("flushing file entity output stream:" + tempFile.getAbsolutePath());

    if (file != null) {
      LOGGER.warn("already flushed, ignoring.");
      return;
    }

    outputStream.flush();

    try {
      file = fileStorageService.put(tempFile);
    } catch (Exception e) {
      LOGGER.error("error putting file", e);
    }
  }

  public com.walterjwhite.file.api.model.File getFile() {
    return file;
  }
}
