package com.transformuk.hee.tis.reference.service.service;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;

import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractReferenceService<T> {

  /**
   * Find the entity with the given ID.
   *
   * @param id The ID to find the entity for.
   * @return An optional entity, will be empty if no entity with the given ID was found.
   */
  public Optional<T> findById(Long id) {
    return getRepository().findById(id);
  }

  /**
   * Find all entities.
   *
   * @param pageable The page data.
   * @return A page of entities.
   */
  public Page<T> findAll(Pageable pageable) {
    return getRepository().findAll(pageable);
  }

  /**
   * Save the entity.
   *
   * @param entity the entity to save.
   * @return The saved entity.
   */
  public T save(T entity) {
    return getRepository().save(entity);
  }

  /**
   * Delete the entity with the given ID.
   *
   * @param id The ID of the entity to delete.
   */
  public void deleteById(Long id) {
    getRepository().deleteById(id);
  }

  /**
   * Perform an advanced search.
   *
   * @param searchString  The string to search for.
   * @param columnFilters The column filters to apply.
   * @param pageable      The pageable object.
   * @return The page of found entities.
   */
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

  /**
   * Get the entity's field to search within.
   *
   * @return A list of searchable fields.
   */
  protected abstract List<String> getSearchFields();

  /**
   * Get the repository for the entity.
   *
   * @return The repository for the entity.
   */
  protected abstract JpaRepository<T, Long> getRepository();

  /**
   * Get the specification executor for the entity.
   *
   * @return The specification executor for the entity.
   */
  protected abstract JpaSpecificationExecutor<T> getSpecificationExecutor();
}
