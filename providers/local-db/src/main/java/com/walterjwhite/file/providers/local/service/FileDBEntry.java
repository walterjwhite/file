package com.walterjwhite.file.providers.local.service;

import com.walterjwhite.file.api.model.File;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(doNotUseGetters = true, callSuper = true)
@NoArgsConstructor
// @PersistenceCapable
@Entity
public class FileDBEntry extends File {
  @Lob @Column protected byte[] data;

  public FileDBEntry(String checksum, byte[] data) {
    super(checksum);
    this.data = data;
  }
}
