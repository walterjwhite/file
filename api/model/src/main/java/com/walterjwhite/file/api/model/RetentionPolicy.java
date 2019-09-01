package com.walterjwhite.file.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractNamedEntity;
import java.time.Duration;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(doNotUseGetters = true, callSuper = true)
// @PersistenceCapable
@Entity
public class RetentionPolicy extends AbstractNamedEntity {
  protected Duration retentionDuration;
}
