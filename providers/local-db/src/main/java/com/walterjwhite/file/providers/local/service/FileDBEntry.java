package com.walterjwhite.file.providers.local.service;

import com.walterjwhite.file.api.model.File;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class FileDBEntry extends File {
  @Lob @Column protected byte[] data;

  public FileDBEntry(String checksum, byte[] data) {
    super(checksum);
    this.data = data;
  }

  public FileDBEntry() {
    super();
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }
}
