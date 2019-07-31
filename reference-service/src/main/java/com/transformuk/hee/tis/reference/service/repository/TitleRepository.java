package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the Title entity.
 */
@SuppressWarnings("unused")
public interface TitleRepository extends JpaRepository<Title, Long>, JpaSpecificationExecutor {

  Title findFirstByCode(String code);
}
