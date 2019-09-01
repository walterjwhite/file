package com.walterjwhite.file.api.service;

import com.walterjwhite.file.api.model.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface FileStorageService {
  /** Write (upload) file to storage provider. */
  File put(File file)
      throws IOException, InvalidAlgorithmParameterException, InvalidKeyException,
          NoSuchAlgorithmException;

  /**
   * Write (upload) local file to storage provider whilst converting it to a cloud representation of
   * a file.
   */
  File put(java.io.File file) throws Exception;

  /** This is NOT required, the FileEntityOutputStream already handles this */
  //  File put(FileEntityOutputStream outputStream);

  /** Read (download) file from storage provider. */
  void get(File file) throws Exception;

  /** Delete file from storage provider. */
  void delete(File file);
}
