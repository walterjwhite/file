package com.walterjwhite.file.impl.service;

import com.walterjwhite.datastore.query.AbstractSingularQuery;
import com.walterjwhite.file.api.model.File;

public class FindFileByChecksumQuery extends AbstractSingularQuery<File> {
  protected final String checksum;

  public FindFileByChecksumQuery(String checksum) {
    super(File.class, false);
    this.checksum = checksum;
  }

  //    Predicate checksumCondition =
  //        criteriaBuilder.equal(fileCriteriaConfiguration.getRoot().get(File_.checksum),
  // checksum);
}
