package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Settled;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Settled entity.
 */
@SuppressWarnings("unused")
public interface SettledRepository extends JpaRepository<Settled, Long> {

}
