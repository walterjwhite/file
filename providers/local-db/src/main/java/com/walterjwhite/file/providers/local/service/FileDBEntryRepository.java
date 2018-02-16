package com.walterjwhite.file.providers.local.service;

import com.google.inject.Inject;
import com.walterjwhite.datastore.criteria.AbstractRepository;
import com.walterjwhite.datastore.criteria.CriteriaQueryConfiguration;
import com.walterjwhite.file.api.model.File;
import com.walterjwhite.file.api.model.File_;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

public class FileDBEntryRepository extends AbstractRepository<FileDBEntry> {
  @Inject
  protected FileDBEntryRepository(EntityManager entityManager, CriteriaBuilder criteriaBuilder) {
    super(entityManager, criteriaBuilder, FileDBEntry.class);
  }

  public FileDBEntry findByFile(File file) {
    final CriteriaQueryConfiguration<FileDBEntry> jobCriteriaQueryConfiguration =
        getCriteriaQuery();

    Predicate condition =
        criteriaBuilder.equal(jobCriteriaQueryConfiguration.getRoot().get(File_.id), file);
    jobCriteriaQueryConfiguration.getCriteriaQuery().where(condition);

    return entityManager
        .createQuery(jobCriteriaQueryConfiguration.getCriteriaQuery())
        .getSingleResult();
  }
}
