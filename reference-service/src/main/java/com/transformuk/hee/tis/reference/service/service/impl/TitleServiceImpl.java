package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.Title;
import com.transformuk.hee.tis.reference.service.repository.TitleRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class TitleServiceImpl extends AbstractReferenceService<Title> {

  private TitleRepository repository;

  TitleServiceImpl(TitleRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<Title, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<Title> getSpecificationExecutor() {
    return repository;
  }
}
