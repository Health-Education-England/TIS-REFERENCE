package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.Trust;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Trust entity.
 */
@SuppressWarnings("unused")
public interface TrustRepository extends JpaRepository<Trust, Long> {

}
