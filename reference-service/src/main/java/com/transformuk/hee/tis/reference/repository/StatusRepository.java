package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Status entity.
 */
@SuppressWarnings("unused")
public interface StatusRepository extends JpaRepository<Status, Long> {

}
