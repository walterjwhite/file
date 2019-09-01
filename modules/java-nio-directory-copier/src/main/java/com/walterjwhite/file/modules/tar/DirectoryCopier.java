package com.walterjwhite.file.modules.tar;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.FileUtils;

public class DirectoryCopier implements FileVisitor<Path> {
  private final Path source;
  private final Path target;

  // TODO: pass this in as a parameter
  private boolean validateCopiedFiles = false;

  private Set<Path> existingPaths;

  public DirectoryCopier(Path source, Path target) {
    super();

    this.source = source;
    this.target = target;

    this.existingPaths = new HashSet<>();
  }

  @Override
  public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attrs)
      throws IOException {
    Path newDirectory = getTarget(directory);

    if (newDirectory.toFile().exists()) {
      onAlreadyExisting(newDirectory);
      return FileVisitResult.CONTINUE;
    }

    Files.copy(
        directory,
        newDirectory,
        StandardCopyOption.COPY_ATTRIBUTES,
        StandardCopyOption.REPLACE_EXISTING);

    return FileVisitResult.CONTINUE;
  }

  protected void onAlreadyExisting(Path path) {
    existingPaths.add(path);
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes fileAttributes)
      throws IOException {
    final Path targetFile = getTarget(file);

    handleFile(file, targetFile);
    setPermissions(file, targetFile);

    return FileVisitResult.CONTINUE;
  }

  protected void handleFile(Path file, Path targetFile) throws IOException {
    if (Files.isSymbolicLink(file)) copySymlink(file, targetFile);
    else copyFile(file, targetFile);
  }

  protected void setPermissions(Path file, Path targetFile) throws IOException {
    Files.setPosixFilePermissions(targetFile, Files.getPosixFilePermissions(file));
  }

  protected void copySymlink(Path file, Path targetFile) throws IOException {
    prepareSymlink(targetFile);
    doSymlink(file, targetFile);
  }

  protected void prepareSymlink(Path targetFile) throws IOException {
    if (targetFile.toFile().isDirectory()) FileUtils.deleteDirectory(targetFile.toFile());
    else targetFile.toFile().delete();
  }

  protected void doSymlink(Path file, Path targetFile) throws IOException {
    Files.createLink(targetFile, file);
  }

  protected void copyFile(Path file, Path targetFile) throws IOException {
    doCopy(file, targetFile);
    validateCopiedFiles(file, targetFile);
  }

  protected void doCopy(Path file, Path targetFile) throws IOException {
    Files.copy(
        file, targetFile, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
  }

  protected void validateCopiedFiles(Path file, Path targetFile) throws IOException {
    if (!validateCopiedFiles) return;

    //        final String sourceChecksum = ChecksumUtil.sha256sum(file.toFile());
    //        final String targetChecksum = ChecksumUtil.sha256sum(targetFile.toFile());
    //
    //        if (!sourceChecksum.equals(targetChecksum)) {
    //          throw new IllegalStateException(
    //              "source checksum: " + sourceChecksum + " does not match (target): " +
    //   targetChecksum);
    //        }
  }

  protected Path getTarget(final Path file) {
    return (target.resolve(source.relativize(file)));
  }

  @Override
  public FileVisitResult postVisitDirectory(Path directory, IOException ioException)
      throws IOException {
    Path newDirectory = getTarget(directory);
    Files.setLastModifiedTime(newDirectory, Files.getLastModifiedTime(directory));
    setDirectoryPermissions(directory, newDirectory);

    return FileVisitResult.CONTINUE;
  }

  protected void setDirectoryPermissions(Path directory, Path newDirectory) throws IOException {
    if (!existingPaths.contains(newDirectory)) {
      Files.setPosixFilePermissions(newDirectory, Files.getPosixFilePermissions(directory));
    }
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException ioException) {
    if (ioException instanceof FileSystemLoopException)
      handleFileSystemLoop((FileSystemLoopException) ioException);
    else handleOtherIOException(ioException);

    return FileVisitResult.CONTINUE;
  }

  protected void handleFileSystemLoop(final FileSystemLoopException fileSystemLoopException) {}

  protected void handleOtherIOException(final IOException ioException) {}
}
