package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Site entity.
 */
@SuppressWarnings("unused")
public interface SiteRepository extends JpaRepository<Site, Long> {

}
