package com.walterjwhite.file.modules.tailer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;

public class TailFileWatcher implements Runnable {
  protected final WatchService watchService;
  protected final TailFileChangeListener tailFileChangeListener;
  protected final Path path;

  public TailFileWatcher(final File file, final OutputStream outputStream) throws IOException {
    super();
    this.path = file.toPath();
    this.tailFileChangeListener = new TailFileChangeListener(file, outputStream);
    this.watchService = FileSystems.getDefault().newWatchService();
    file.getParentFile().toPath().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
  }

  protected void notifyListener(WatchKey watchKey) {
    tailFileChangeListener.onFileModify();
  }

  @Override
  public void run() {
    while (true) {
      try {
        final WatchKey watchKey = watchService.take();

        for (WatchEvent watchEvent : watchKey.pollEvents()) {
          if (watchEvent.kind().equals(StandardWatchEventKinds.OVERFLOW)) {
            return;
          }

          if (watchEvent.context().equals(path.getFileName())) notifyListener(watchKey);
        }

        if (!watchKey.reset()) {
          throw new IllegalStateException("Unable to continue monitoring file");
        }
      } catch (InterruptedException e) {
        throw new RuntimeException("Error watching file for changes", e);
      }
    }
  }
}
