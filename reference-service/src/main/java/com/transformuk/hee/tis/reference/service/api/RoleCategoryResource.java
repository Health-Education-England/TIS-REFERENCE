package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.RoleCategoryDTO;
import com.transformuk.hee.tis.reference.service.model.RoleCategory;
import com.transformuk.hee.tis.reference.service.repository.RoleCategoryRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.RoleCategoryMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Role Categories.
 */
@Slf4j
@RestController
@RequestMapping("/api/role-categories")
public class RoleCategoryResource {

  private final RoleCategoryRepository repository;
  private final RoleCategoryMapper mapper;

  public RoleCategoryResource(RoleCategoryRepository repository, RoleCategoryMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  /**
   * GET  : get all roles categories.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of roles categories the in body.
   */
  @ApiOperation(value = "Lists role categories", notes = "Returns a list of role categories")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "role category list")
  })
  @GetMapping
  public ResponseEntity<List<RoleCategoryDTO>> getAllRoleCategories() {
    log.info("REST request to get all role categories received.");
    List<RoleCategory> roleCategories = repository.findAll(Sort.by("name"));
    List<RoleCategoryDTO> roleCategoryDtos = mapper
        .roleCategorysToRoleCategoryDTOs(roleCategories);
    return ResponseEntity.ok(roleCategoryDtos);
  }
}
