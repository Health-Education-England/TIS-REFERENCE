package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.MedicalSchool;
import com.transformuk.hee.tis.reference.service.repository.MedicalSchoolRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for MedicalSchool.
 */
@Service
public class MedicalSchoolServiceImpl extends AbstractReferenceService<MedicalSchool> {

  private MedicalSchoolRepository repository;

  MedicalSchoolServiceImpl(MedicalSchoolRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<MedicalSchool, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<MedicalSchool> getSpecificationExecutor() {
    return repository;
  }
}
