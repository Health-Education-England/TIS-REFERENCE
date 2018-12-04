package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.Role;
import com.transformuk.hee.tis.reference.service.repository.RoleRepository;
import com.transformuk.hee.tis.reference.service.service.impl.RoleServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.RoleMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.reference.service.api.util.StringUtil.sanitize;

/**
 * REST controller for managing Role.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

    private static final String ENTITY_NAME = "role";
    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RoleServiceImpl roleService;

    public RoleResource(RoleRepository roleRepository, RoleMapper roleMapper, RoleServiceImpl roleService) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.roleService = roleService;
    }

    /**
     * POST  /roles : Create a new role.
     *
     * @param roleDTO the roleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roleDTO, or with status 400 (Bad Request) if the role has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('reference:add:modify:entities')")
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDTO) throws URISyntaxException {
        log.debug("REST request to save Role : {}", roleDTO);
        if (roleDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new role cannot already have an ID")).body(null);
        }
        Role role = roleMapper.roleDTOToRole(roleDTO);
        role = roleRepository.save(role);
        RoleDTO result = roleMapper.roleToRoleDTO(role);
        return ResponseEntity.created(new URI("/api/roles/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /roles : Updates an existing role.
     *
     * @param roleDTO the roleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roleDTO,
     * or with status 400 (Bad Request) if the roleDTO is not valid,
     * or with status 500 (Internal Server Error) if the roleDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roles")
    @PreAuthorize("hasAuthority('reference:add:modify:entities')")
    public ResponseEntity<RoleDTO> updateRole(@Valid @RequestBody RoleDTO roleDTO) throws URISyntaxException {
        log.debug("REST request to update Role : {}", roleDTO);
        if (roleDTO.getId() == null) {
            return createRole(roleDTO);
        }
        Role role = roleMapper.roleDTOToRole(roleDTO);
        role = roleRepository.save(role);
        RoleDTO result = roleMapper.roleToRoleDTO(role);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roleDTO.getId().toString()))
                .body(result);
    }


    /**
     * GET  /roles : get all roles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles(
            Pageable pageable,
            @RequestParam(value = "searchQuery", required = false) String searchQuery,
            @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
        log.info("REST request to get a page of roles begin");
        searchQuery = sanitize(searchQuery);
        List<Class> filterEnumList = Lists.newArrayList(Status.class);
        List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
        Page<Role> page;
        if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
            page = roleRepository.findAll(pageable);
        } else {
            page = roleService.advancedSearch(searchQuery, columnFilters, pageable);
        }
        Page<RoleDTO> results = page.map(roleMapper::roleToRoleDTO);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/roles");
        return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/roles/categories/{categoryId}")
    public ResponseEntity<List<RoleDTO>> getAllRolesByCategory(
            @PathVariable(value = "categoryId") final Long categoryId) {
        log.debug("Received request to load '{}' for category with ID '{}'",
                RoleDTO.class.getSimpleName(), categoryId);

        log.debug("Accessing service to load '{}' for category with ID '{}'",
                RoleDTO.class.getSimpleName(), categoryId);

        return ResponseEntity.ok(roleService.findAllByCategoryId(categoryId));
    }


    /**
     * GET  /current/roles : get all the current roles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @GetMapping("/current/roles")
    public List<RoleDTO> getAllCurrentRoles() {
        log.debug("REST request to get all current Roles");
        Role role = new Role().status(Status.CURRENT);
        List<Role> roles = roleRepository.findAll(Example.of(role));
        return roleMapper.rolesToRoleDTOs(roles);
    }

    /**
     * GET  /roles/:id : get the "id" role.
     *
     * @param id the id of the roleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/roles/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable Long id) {
        log.debug("REST request to get Role : {}", id);
        Role role = roleRepository.findById(id).orElse(null);
        RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(roleDTO));
    }

    /**
     * DELETE  /roles/:id : delete the "id" role.
     *
     * @param id the id of the roleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('reference:delete:entities')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.debug("REST request to delete Role : {}", id);
        roleRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    /**
     * POST  /bulk-roles : Bulk create a new roles.
     *
     * @param roleDTOS List of the roleDTOS to create
     * @return the ResponseEntity with status 200 (Created) and with body the new roleDTOS, or with status 400 (Bad Request) if the RoleDTO has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bulk-roles")
    @PreAuthorize("hasAuthority('reference:add:modify:entities')")
    public ResponseEntity<List<RoleDTO>> bulkCreateRole(@Valid @RequestBody List<RoleDTO> roleDTOS) throws URISyntaxException {
        log.debug("REST request to bulk save RoleDtos : {}", roleDTOS);
        if (!Collections.isEmpty(roleDTOS)) {
            List<Long> entityIds = roleDTOS.stream()
                    .filter(roleDTO -> roleDTO.getId() != null)
                    .map(roleDTO -> roleDTO.getId())
                    .collect(Collectors.toList());
            if (!Collections.isEmpty(entityIds)) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new roles cannot already have an ID")).body(null);
            }
        }
        List<Role> roles = roleMapper.roleDTOsToRoles(roleDTOS);
        roles = roleRepository.saveAll(roles);
        List<RoleDTO> result = roleMapper.rolesToRoleDTOs(roles);
        return ResponseEntity.ok()
                .body(result);
    }

    /**
     * PUT  /bulk-roles : Updates an existing roles.
     *
     * @param roleDTOS List of the roleDTOS to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roleDTOS,
     * or with status 400 (Bad Request) if the roleDTOS is not valid,
     * or with status 500 (Internal Server Error) if the roleDTOS couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bulk-roles")
    @PreAuthorize("hasAuthority('reference:add:modify:entities')")
    public ResponseEntity<List<RoleDTO>> bulkUpdateRole(@Valid @RequestBody List<RoleDTO> roleDTOS) throws URISyntaxException {
        log.debug("REST request to bulk update RoleDtos : {}", roleDTOS);
        if (Collections.isEmpty(roleDTOS)) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
                    "The request body for this end point cannot be empty")).body(null);
        } else if (!Collections.isEmpty(roleDTOS)) {
            List<RoleDTO> entitiesWithNoId = roleDTOS.stream().filter(roleDTO -> roleDTO.getId() == null).collect(Collectors.toList());
            if (!Collections.isEmpty(entitiesWithNoId)) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                        "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
            }
        }
        List<Role> roleList = roleMapper.roleDTOsToRoles(roleDTOS);
        roleList = roleRepository.saveAll(roleList);
        List<RoleDTO> results = roleMapper.rolesToRoleDTOs(roleList);
        return ResponseEntity.ok()
                .body(results);
    }

}
