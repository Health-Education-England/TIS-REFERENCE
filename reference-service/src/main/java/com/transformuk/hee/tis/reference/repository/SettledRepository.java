package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.Settled;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Settled entity.
 */
@SuppressWarnings("unused")
public interface SettledRepository extends JpaRepository<Settled, Long> {

}
