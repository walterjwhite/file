package com.walterjwhite.file.api.service;

import java.io.*;

public class FileEntityOutputStream extends OutputStream {
  protected final File tempFile;
  protected final OutputStream outputStream;

  protected final FileStorageService fileStorageService;

  protected com.walterjwhite.file.api.model.File file;

  public FileEntityOutputStream(FileStorageService fileStorageService) throws IOException {
    super();

    this.fileStorageService = fileStorageService;
    tempFile = File.createTempFile("temp", "tmp");
    outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
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
    flush();
    outputStream.close();
  }

  @Override
  public void write(int i) throws IOException {
    outputStream.write(i);
  }

  @Override
  public void flush() throws IOException {
    if (!wasAlreadyFlushed()) doFlush();
  }

  protected boolean wasAlreadyFlushed() {
    return file != null;
  }

  protected void doFlush() throws IOException {
    outputStream.flush();

    try {
      file = fileStorageService.put(tempFile);
      tempFile.delete();
    } catch (Exception e) {
      throw new IOException("Error putting file", e);
    }
  }

  public com.walterjwhite.file.api.model.File getFile() {
    return file;
  }
}
