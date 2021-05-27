package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.service.model.Role;
import com.transformuk.hee.tis.reference.service.model.RoleCategory;
import com.transformuk.hee.tis.reference.service.repository.RoleRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import com.transformuk.hee.tis.reference.service.service.mapper.RoleMapper;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for Role.
 */
@Service
public class RoleServiceImpl extends AbstractReferenceService<Role> {

  private RoleRepository repository;
  private RoleMapper mapper;

  RoleServiceImpl(RoleRepository repository, RoleMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<Role, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<Role> getSpecificationExecutor() {
    return repository;
  }

  public List<RoleDTO> findAllByCategoryId(Long categoryId) {
    RoleCategory roleCategory = new RoleCategory();
    roleCategory.setId(categoryId);

    return repository.findAllByRoleCategory(roleCategory)
        .stream()
        .map(mapper::roleToRoleDTO)
        .collect(Collectors.toList());
  }
}
