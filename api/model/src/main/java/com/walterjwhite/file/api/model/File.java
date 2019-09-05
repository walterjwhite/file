package com.walterjwhite.file.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
// @PersistenceCapable
@NoArgsConstructor
@Entity
public class File extends AbstractEntity {
  /** where this file exists (before writing to provider or after reading from provider). */
  @EqualsAndHashCode.Exclude /*@NotPersistent*/ @Transient protected transient String source;

  /** Used as the unique identifier for the file */
  @Column(unique = true, nullable = false, updatable = false)
  protected String checksum;

  @EqualsAndHashCode.Exclude
  @Column(nullable = false, updatable = false)
  protected LocalDateTime createdDateTime = LocalDateTime.now();

  @EqualsAndHashCode.Exclude @Column protected String name;
  @EqualsAndHashCode.Exclude @Column protected String extension;

  // TODO: automatically purge files
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

  // Splits the filename into the name and extension
  public File withFilename(final String filename) {
    final int index = filename.lastIndexOf(".");
    if (index > 0) {
      name = filename.substring(0, index);
      extension = filename.substring(index + 1);
    }

    return this;
  }
}
