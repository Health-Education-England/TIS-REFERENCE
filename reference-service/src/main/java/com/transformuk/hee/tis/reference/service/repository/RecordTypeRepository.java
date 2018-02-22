package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the RecordType entity.
 */
@SuppressWarnings("unused")
public interface RecordTypeRepository extends JpaRepository<RecordType, Long>, JpaSpecificationExecutor {

}
