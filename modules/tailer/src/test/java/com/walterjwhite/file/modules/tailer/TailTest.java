package com.walterjwhite.file.modules.tailer;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

/** Primitive example of tailing a log file using the watcher service. */
public class TailTest {

  @Test(timeout = 5000)
  public void doTest() throws IOException, InterruptedException {
    final Tailer tailer = new Tailer(new File("/tmp/test"));
    tailer.run();
    System.out.println("tailing file");
  }
}
