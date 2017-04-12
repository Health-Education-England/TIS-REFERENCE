package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.Title;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Title entity.
 */
@SuppressWarnings("unused")
public interface TitleRepository extends JpaRepository<Title, Long> {

}
