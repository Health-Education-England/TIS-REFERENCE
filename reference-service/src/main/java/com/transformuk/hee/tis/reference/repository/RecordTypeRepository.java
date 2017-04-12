package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the RecordType entity.
 */
@SuppressWarnings("unused")
public interface RecordTypeRepository extends JpaRepository<RecordType, Long> {

}
