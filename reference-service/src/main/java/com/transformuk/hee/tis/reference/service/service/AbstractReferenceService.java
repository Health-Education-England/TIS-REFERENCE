package com.transformuk.hee.tis.reference.service.service;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;

import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractReferenceService<T> {

  @Transactional(readOnly = true)
  public Page<T> advancedSearch(String searchString, List<ColumnFilter> columnFilters,
      Pageable pageable) {
    List<Specification<T>> specs = buildSpecifications(columnFilters, searchString);
    Page<T> result;

    if (!specs.isEmpty()) {
      Specification<T> fullSpec = Specification.where(specs.get(0));

      // Add the rest of the specs that made it in.
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }

      result = getSpecificationExecutor().findAll(fullSpec, pageable);
    } else {
      result = getRepository().findAll(pageable);
    }

    return result;
  }

  private List<Specification<T>> buildSpecifications(List<ColumnFilter> columnFilters,
      String searchString) {
    List<Specification<T>> specs = new ArrayList<>();
    // Add the text search criteria.
    if (StringUtils.isNotEmpty(searchString)) {
      ListIterator<String> fieldIterator = getSearchFields().listIterator();

      if (fieldIterator.hasNext()) {
        Specification<T> spec = Specification
            .where(containsLike(fieldIterator.next(), searchString));

        while (fieldIterator.hasNext()) {
          spec = spec.or(containsLike(fieldIterator.next(), searchString));
        }
        specs.add(spec);
      }
    }

    // Add the column filters criteria.
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    return specs;
  }

  protected abstract List<String> getSearchFields();

  protected abstract JpaRepository<T, Long> getRepository();

  protected abstract JpaSpecificationExecutor<T> getSpecificationExecutor();
}
