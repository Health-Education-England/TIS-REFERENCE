package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.domain.Title;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Title entity.
 */
@SuppressWarnings("unused")
public interface TitleRepository extends JpaRepository<Title, Long> {

}
