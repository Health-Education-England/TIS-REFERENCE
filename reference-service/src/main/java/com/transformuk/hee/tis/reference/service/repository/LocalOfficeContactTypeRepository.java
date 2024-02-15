package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.LocalOfficeContactType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LocalOfficeContactType entity.
 */
@Repository
public interface LocalOfficeContactTypeRepository extends
    JpaRepository<LocalOfficeContactType, UUID>, JpaSpecificationExecutor<LocalOfficeContactType> {

}
