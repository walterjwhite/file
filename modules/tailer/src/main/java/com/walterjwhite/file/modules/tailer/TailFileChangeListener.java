package com.walterjwhite.file.modules.tailer;

import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TailFileChangeListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(TailFileChangeListener.class);

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
      throw (new RuntimeException("Error reading file", e));
    }
  }
}
