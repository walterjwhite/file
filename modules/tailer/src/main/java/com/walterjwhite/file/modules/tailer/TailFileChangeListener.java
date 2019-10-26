package com.walterjwhite.file.modules.tailer;

import java.io.*;

public class TailFileChangeListener {
  protected final File file;
  protected final FileReader fileReader;
  protected final OutputStream outputStream;

  public TailFileChangeListener(File file) throws IOException {
    this(file, System.out);
  }

  public TailFileChangeListener(File file, OutputStream outputStream) throws IOException {
    this.file = file;
    this.fileReader = new FileReader(file);
    // jump to the end of the file
    this.fileReader.skip(file.length());
    this.outputStream = outputStream;
  }

  public void onFileModify() {
    try {
      while (fileReader.ready()) {
        outputStream.write(fileReader.read());
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading file", e);
    }
  }
}
