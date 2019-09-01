package com.walterjwhite.file.modules.cli.handler;

import com.walterjwhite.encryption.api.service.CompressionService;
import com.walterjwhite.encryption.service.EncryptionService;
import com.walterjwhite.file.api.service.FileStorageService;
import com.walterjwhite.inject.cli.property.CommandLineHandlerShutdownTimeout;
import com.walterjwhite.inject.cli.service.AbstractCommandLineHandler;
import com.walterjwhite.property.impl.annotation.Property;
import java.io.File;
import javax.inject.Inject;

/** Helper to list our client id. */
public class EncryptHandler extends AbstractCommandLineHandler {
  protected final FileStorageService fileStorageService;
  protected final EncryptionService encryptionService;
  protected final CompressionService compressionService;

  @Inject
  public EncryptHandler(
      @Property(CommandLineHandlerShutdownTimeout.class) int shutdownTimeoutInSeconds,
      FileStorageService fileStorageService,
      EncryptionService encryptionService,
      CompressionService compressionService) {
    super(shutdownTimeoutInSeconds);
    this.fileStorageService = fileStorageService;
    this.encryptionService = encryptionService;
    this.compressionService = compressionService;
  }

  @Override
  protected void doRun(final String... arguments) throws Exception {
    validateInput(arguments);
    doRunInstance(arguments);
  }

  protected void validateInput(final String[] arguments) {
    if (arguments == null || arguments.length == 0) {
      System.err.println("Please specify at least 1 argument to encrypt");
      System.exit(1);
    }
  }

  protected void doRunInstance(final String[] arguments) throws Exception {
    for (final String argument : arguments) {
      doRun(argument);
    }
  }

  protected void doRunInstance(final String argument) throws Exception {
    final File source = new File(argument);
    System.out.println(source + " " + fileStorageService.put(source).getChecksum());
  }
}
