package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.Role;
import com.transformuk.hee.tis.reference.service.model.RoleCategory;
import com.transformuk.hee.tis.reference.service.repository.RoleRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.RoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;

@Service
public class RoleServiceImpl {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Transactional(readOnly = true)
    public Page<Role> advancedSearch(String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {

        List<Specification<Role>> specs = new ArrayList<>();
        //add the text search criteria
        if (StringUtils.isNotEmpty(searchString)) {
            specs.add(Specifications.where(containsLike("code", searchString)).
                    or(containsLike("label", searchString)));
        }
        //add the column filters criteria
        if (columnFilters != null && !columnFilters.isEmpty()) {
            columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
        }

        Page<Role> result;
        if (!specs.isEmpty()) {
            Specifications<Role> fullSpec = Specifications.where(specs.get(0));
            //add the rest of the specs that made it in
            for (int i = 1; i < specs.size(); i++) {
                fullSpec = fullSpec.and(specs.get(i));
            }
            result = roleRepository.findAll(fullSpec, pageable);
        } else {
            result = roleRepository.findAll(pageable);
        }

        return result;
    }

    public List<RoleDTO> findAllByCategoryId(Long categoryId) {
        RoleCategory roleCategory = new RoleCategory();
        roleCategory.setId(categoryId);

        return roleRepository.findAllByRoleCategory(roleCategory)
                .stream()
                .map(roleMapper::roleToRoleDTO)
                .collect(Collectors.toList());
    }
}
