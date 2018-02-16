package com.walterjwhite.file.modules.tailer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/** Tail a file. */
public class Tailer {
  protected final TailFileWatcher tailFileWatcher;
  protected final Thread tailFailWatcherThread;
  protected final File file;

  public Tailer(File file) throws IOException {
    this(file, System.out);
  }

  public Tailer(File file, OutputStream outputStream) throws IOException {
    this.file = file;
    this.tailFileWatcher = new TailFileWatcher(file, outputStream);
    this.tailFailWatcherThread = new Thread(tailFileWatcher);
    this.tailFailWatcherThread.start();
  }

  public void run() throws InterruptedException {
    this.tailFailWatcherThread.join();
  }
}
