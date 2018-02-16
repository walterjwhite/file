package com.walterjwhite.file.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class File extends AbstractEntity {
  /** where this file exists (before writing to provider or after reading from provider). */
  protected transient String source;

  /** Used as the unique identifier for the file */
  @Column(unique = true, nullable = false, updatable = false)
  protected String checksum;

  @Column(nullable = false, updatable = false)
  protected LocalDateTime createdDateTime = LocalDateTime.now();

  @Column protected String name;
  @Column protected String extension;

  /** TODO: automatically purge files * */
  // retention policy
  // date created

  public File(String source, String checksum) {
    super();
    this.source = source;
    this.checksum = checksum;
  }

  public File(String source) {
    super();
    this.source = source;
  }

  public File() {
    super();
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getChecksum() {
    return checksum;
  }

  public void setChecksum(String checksum) {
    this.checksum = checksum;
  }

  public LocalDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  public void setCreatedDateTime(LocalDateTime createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    File file = (File) o;
    return Objects.equals(checksum, file.checksum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(checksum);
  }
}
