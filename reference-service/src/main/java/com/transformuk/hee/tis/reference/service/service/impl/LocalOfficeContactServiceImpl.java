package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.LocalOfficeContact;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeContactRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for LocalOfficeContact.
 */
@Service
public class LocalOfficeContactServiceImpl extends
    AbstractReferenceService<LocalOfficeContact, UUID> {

  private final LocalOfficeContactRepository repository;

  LocalOfficeContactServiceImpl(LocalOfficeContactRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("contact", "localOffice.abbreviation", "localOffice.name",
        "contactType.code", "contactType.label");
  }

  @Override
  protected JpaRepository<LocalOfficeContact, UUID> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<LocalOfficeContact> getSpecificationExecutor() {
    return repository;
  }
}
